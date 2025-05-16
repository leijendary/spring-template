package com.leijendary.domain.image

import com.leijendary.context.DatabaseContext
import com.leijendary.domain.image.Image.Companion.ENTITY
import com.leijendary.domain.image.Image.Companion.POINTER_STORAGE_NAME
import com.leijendary.error.CODE_IMAGE_MEDIA_TYPE
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.error.exception.StatusException
import com.leijendary.extension.logger
import com.leijendary.projection.ImageProjection
import com.leijendary.storage.BlockStorage
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.MediaType.IMAGE_JPEG_VALUE
import org.springframework.http.MediaType.IMAGE_PNG_VALUE
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture.supplyAsync

interface ImageService {
    fun createUploadUrl(request: ImageCreateUrlRequest): ImageCreateUrlResponse
    fun validate(request: ImageRequest): ImageMultiValidateResponse
    fun validate(name: String): ImageValidateResponse
    fun <T : ImageProjection> getPublicUrl(image: T): T
    fun <T : ImageProjection> getPrivateUrl(image: T): T
    fun delete(name: String)
}

private const val PREFIX = "image/"
private val IMAGE_MEDIA_TYPES = arrayOf(IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE)

@Service
class ImageServiceImpl(
    private val blockStorage: BlockStorage,
    private val databaseContext: DatabaseContext,
    private val imageMetadataRepository: ImageMetadataRepository,
    private val imageRepository: ImageRepository,
) : ImageService {
    private val log = logger()

    override fun createUploadUrl(request: ImageCreateUrlRequest): ImageCreateUrlResponse {
        val (image, key) = databaseContext.transactional {
            val image = imageRepository.findByName(request.name).orElseGet { imageRepository.save(Image(request.name)) }

            // Remove all existing metadata first
            imageMetadataRepository.deleteById(image.id)

            // Then replace with new ones
            val metadata = request.metadata.map {
                ImageMetadata(it.name, it.value).apply { this.id = image.id }
            }

            imageMetadataRepository.saveAll(metadata)

            image to "$PREFIX${image.name}"
        }

        if (image.validated) {
            return ImageCreateUrlResponse(key, true)
        }

        val url = blockStorage.signPut(key)

        log.info("Created upload URL {} for request {}", url, request)

        return ImageCreateUrlResponse(url)
    }

    override fun validate(request: ImageRequest): ImageMultiValidateResponse {
        val originalFuture = supplyAsync { validate(request.original) }
        val previewFuture = supplyAsync { validate(request.preview) }
        val thumbnailFuture = supplyAsync { validate(request.thumbnail) }

        return ImageMultiValidateResponse(originalFuture.get(), previewFuture.get(), thumbnailFuture.get())
    }

    override fun validate(name: String): ImageValidateResponse {
        val image = imageRepository.findByNameOrThrow(name)
        val path = "$PREFIX$name"

        if (image.validated) {
            return ImageValidateResponse(image.id, name, path)
        }

        val response = blockStorage.head(path)
            ?: throw ResourceNotFoundException(name, ENTITY, POINTER_STORAGE_NAME)
        val contentType = response.contentType()

        if (contentType !in IMAGE_MEDIA_TYPES) {
            log.info("$ENTITY {} with type {} is not in {}. Deleting.", name, contentType, IMAGE_MEDIA_TYPES)

            supplyAsync {
                blockStorage.delete(path)
                delete(name)
            }

            throw StatusException(CODE_IMAGE_MEDIA_TYPE, BAD_REQUEST, POINTER_STORAGE_NAME)
        }

        val id = imageRepository.setValidated(name, contentType)

        log.info("Validated $ENTITY name {}", name)

        return ImageValidateResponse(id, name, path)
    }

    override fun <T : ImageProjection> getPublicUrl(image: T): T {
        return blockStorage.cdn(image, PREFIX)
    }

    override fun <T : ImageProjection> getPrivateUrl(image: T): T {
        return blockStorage.sign(image, PREFIX)
    }

    override fun delete(name: String) {
        imageRepository.deleteByName(name)

        log.info("Deleted $ENTITY name {} from database", name)

        blockStorage.delete("$PREFIX$name")

        log.info("Deleted $ENTITY name {} from storage", name)
    }
}
