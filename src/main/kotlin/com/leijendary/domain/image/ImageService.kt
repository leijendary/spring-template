package com.leijendary.domain.image

import com.leijendary.domain.image.Image.Companion.ENTITY
import com.leijendary.domain.image.Image.Companion.ERROR_SOURCE_STORAGE_NAME
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.error.exception.StatusException
import com.leijendary.extension.transactional
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
    private val imageMetadataRepository: ImageMetadataRepository,
    private val imageRepository: ImageRepository,
) : ImageService {
    override fun createUploadUrl(request: ImageCreateUrlRequest): ImageCreateUrlResponse {
        val (image, key) = transactional {
            val image = imageRepository.findByName(request.name).orElseGet { imageRepository.save(Image(request.name)) }

            // Remove all existing metadata first
            imageMetadataRepository.deleteById(image.id)

            // Then replace with new ones
            val metadata = request.metadata.map { ImageMetadata(image.id, it.name, it.value) }
            imageMetadataRepository.saveAll(metadata)

            image to "$PREFIX${image.name}"
        }

        if (image.validated) {
            return ImageCreateUrlResponse(key, true)
        }

        val url = blockStorage.signPut(key)

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
            ?: throw ResourceNotFoundException(name, ENTITY, ERROR_SOURCE_STORAGE_NAME)
        val contentType = response.contentType()

        if (contentType !in IMAGE_MEDIA_TYPES) {
            supplyAsync {
                blockStorage.delete(path)
                delete(name)
            }

            throw StatusException("validation.image.mediaType", BAD_REQUEST, ERROR_SOURCE_STORAGE_NAME)
        }

        val id = imageRepository.setValidated(name, contentType)

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
        blockStorage.delete("$PREFIX$name")
    }
}
