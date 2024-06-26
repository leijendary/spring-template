package com.leijendary.domain.image

import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.error.exception.StatusException
import com.leijendary.extension.transactional
import com.leijendary.projection.ImageProjection
import com.leijendary.storage.BlockStorage
import com.leijendary.context.RequestContext
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture.supplyAsync

interface ImageService {
    fun createUploadUrl(request: ImageCreateUrlRequest): ImageCreateUrlResponse
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

    override fun validate(name: String): ImageValidateResponse {
        val image = imageRepository.getByName(name)

        if (image.validated) {
            return ImageValidateResponse(image.id)
        }

        val key = "$PREFIX$name"
        val exists = blockStorage.exists(key)

        if (!exists) {
            throw ResourceNotFoundException(name, ENTITY, SOURCE_STORAGE_NAME)
        }

        val response = blockStorage.head(key)
        val contentType = response.contentType()

        if (contentType !in IMAGE_MEDIA_TYPES) {
            supplyAsync {
                blockStorage.delete(key)
                delete(name)
            }

            throw StatusException("validation.image.mediaType", BAD_REQUEST, SOURCE_STORAGE_NAME)
        }

        val id = imageRepository.setValidated(name)

        return ImageValidateResponse(id)
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
