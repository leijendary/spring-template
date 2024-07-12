package com.leijendary.domain.image

import com.leijendary.context.RequestContext
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.error.exception.StatusException
import com.leijendary.extension.transactional
import com.leijendary.projection.ImageProjection
import com.leijendary.storage.BlockStorage
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CONFLICT
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

@Service
class ImageServiceImpl(
    private val blockStorage: BlockStorage,
    private val imageRepository: ImageRepository,
    private val requestContext: RequestContext
) : ImageService {
    override fun createUploadUrl(request: ImageCreateUrlRequest): ImageCreateUrlResponse {
        val image = transactional {
            val image = imageRepository.create(request.name, requestContext.userIdOrThrow)

            if (image.validated) {
                throw StatusException("error.image.conflict", CONFLICT, SOURCE_NAME)
            }

            val metadata = request.metadata.associate { it.name to it.value }
            imageRepository.createMetadata(image.id, metadata)

            image
        }
        val url = blockStorage.signPut("$PREFIX${image.name}")

        return ImageCreateUrlResponse(url)
    }

    override fun validate(request: ImageRequest): ImageMultiValidateResponse {
        val originalFuture = supplyAsync { validate(request.original) }
        val previewFuture = supplyAsync { validate(request.preview) }
        val thumbnailFuture = supplyAsync { validate(request.thumbnail) }

        return ImageMultiValidateResponse(originalFuture.get(), previewFuture.get(), thumbnailFuture.get())
    }

    override fun validate(name: String): ImageValidateResponse {
        val image = imageRepository.getByName(name)
        val path = "$PREFIX$name"

        if (image.validated) {
            return ImageValidateResponse(image.id, name, path)
        }

        val response = blockStorage.head(path) ?: throw ResourceNotFoundException(name, ENTITY, SOURCE_STORAGE_NAME)
        val contentType = response.contentType()

        if (contentType !in IMAGE_MEDIA_TYPES) {
            supplyAsync {
                blockStorage.delete(path)
                delete(name)
            }

            throw StatusException("validation.image.mediaType", BAD_REQUEST, SOURCE_STORAGE_NAME)
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
        val id = imageRepository.getIdByName(name)

        transactional {
            imageRepository.deleteMetadata(id)
            imageRepository.delete(id)
        }

        blockStorage.delete("$PREFIX$name")
    }
}
