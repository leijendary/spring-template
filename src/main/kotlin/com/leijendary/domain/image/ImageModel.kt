package com.leijendary.domain.image

import com.leijendary.projection.ImageProjection
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

private const val PATTERN_NAME = "^[\\w,\\s-]+\\.(jpeg|jpg|png)\$"
private const val MESSAGE_NAME = "validation.image.name"

class ImageCreateUrlRequest {
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 250, message = "validation.maxLength")
    @field:Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    lateinit var name: String

    @field:Valid
    lateinit var metadata: List<ImageMetadataRequest>
}

class ImageMetadataRequest {
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 100, message = "validation.maxLength")
    lateinit var name: String

    @field:NotBlank(message = "validation.required")
    lateinit var value: String
}

class ImageValidateRequest {
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 250, message = "validation.maxLength")
    @field:Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    lateinit var name: String
}

class ImageDeleteRequest {
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 250, message = "validation.maxLength")
    @field:Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    lateinit var name: String
}

class ImageRequest : ImageProjection {
    @field:NotBlank(message = "validation.required")
    @field:Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    override lateinit var original: String

    @field:NotBlank(message = "validation.required")
    @field:Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    override lateinit var preview: String

    @field:NotBlank(message = "validation.required")
    @field:Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    override lateinit var thumbnail: String
}

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
