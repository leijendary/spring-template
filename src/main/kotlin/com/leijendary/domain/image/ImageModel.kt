package com.leijendary.domain.image

import com.leijendary.error.CODE_IMAGE_NAME
import com.leijendary.error.CODE_REQUIRED
import com.leijendary.error.CODE_SIZE_RANGE
import com.leijendary.projection.ImageProjection
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

private const val PATTERN_NAME = "^[\\w,\\s-]+\\.(jpeg|jpg|png)\$"

data class ImageCreateUrlRequest(
    @field:NotBlank(message = CODE_REQUIRED)
    @field:Size(min = 3, max = 250, message = CODE_SIZE_RANGE)
    @field:Pattern(regexp = PATTERN_NAME, message = CODE_IMAGE_NAME)
    val name: String = "",

    @field:Valid
    val metadata: List<ImageMetadataRequest> = emptyList()
)

data class ImageMetadataRequest(
    @field:NotBlank(message = CODE_REQUIRED)
    @field:Size(min = 3, max = 100, message = CODE_SIZE_RANGE)
    val name: String = "",

    @field:NotBlank(message = CODE_REQUIRED)
    val value: String = ""
)

data class ImageValidateRequest(
    @field:NotBlank(message = CODE_REQUIRED)
    @field:Size(min = 3, max = 250, message = CODE_SIZE_RANGE)
    @field:Pattern(regexp = PATTERN_NAME, message = CODE_IMAGE_NAME)
    val name: String = ""
)

data class ImageDeleteRequest(
    @field:NotBlank(message = CODE_REQUIRED)
    @field:Size(min = 3, max = 250, message = CODE_SIZE_RANGE)
    @field:Pattern(regexp = PATTERN_NAME, message = CODE_IMAGE_NAME)
    val name: String = ""
)

data class ImageRequest(
    @field:NotBlank(message = CODE_REQUIRED)
    @field:Pattern(regexp = PATTERN_NAME, message = CODE_IMAGE_NAME)
    override var original: String = "",

    @field:NotBlank(message = CODE_REQUIRED)
    @field:Pattern(regexp = PATTERN_NAME, message = CODE_IMAGE_NAME)
    override var preview: String = "",

    @field:NotBlank(message = CODE_REQUIRED)
    @field:Pattern(regexp = PATTERN_NAME, message = CODE_IMAGE_NAME)
    override var thumbnail: String = "",
) : ImageProjection

data class ImageCreateUrlResponse(val url: String, val validated: Boolean = false)

data class ImageValidateResponse(val id: String, val name: String, val path: String)

data class ImageMultiValidateResponse(
    val original: ImageValidateResponse,
    val preview: ImageValidateResponse,
    val thumbnail: ImageValidateResponse
)

data class ImageResponse(
    override var original: String,
    override var preview: String,
    override var thumbnail: String
) : ImageProjection
