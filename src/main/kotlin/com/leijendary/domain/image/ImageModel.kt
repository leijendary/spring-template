package com.leijendary.domain.image

import com.leijendary.projection.ImageProjection
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.http.MediaType.IMAGE_JPEG_VALUE
import org.springframework.http.MediaType.IMAGE_PNG_VALUE

val IMAGE_MEDIA_TYPES = arrayOf(IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE)

private const val PATTERN_NAME = "^[\\w,\\s-]+\\.(jpeg|jpg|png)\$"
private const val MESSAGE_NAME = "validation.image.name"

class ImageCreateUrlRequest {
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 250, message = "validation.maxLength")
    @field:Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    val name: String = ""

    @field:Valid
    val metadata = arrayListOf<ImageMetadataRequest>()
}

class ImageMetadataRequest {
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 100, message = "validation.maxLength")
    val name: String = ""

    @field:NotBlank(message = "validation.required")
    val value: String = ""
}

class ImageValidateRequest {
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 250, message = "validation.maxLength")
    @field:Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    val name: String = ""
}

class ImageDeleteRequest {
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 250, message = "validation.maxLength")
    @field:Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    val name: String = ""
}

class ImageRequest : ImageProjection {
    @field:NotBlank(message = "validation.required")
    @field:Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    override var original: String = ""

    @field:NotBlank(message = "validation.required")
    @field:Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    override var preview: String = ""

    @field:NotBlank(message = "validation.required")
    @field:Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    override var thumbnail: String = ""
}

data class ImageResult(val id: Long, val name: String, val validated: Boolean)

data class ImageCreateUrlResponse(val url: String)

data class ImageValidateResponse(val id: Long)

data class ImageResponse(
    override var original: String,
    override var preview: String,
    override var thumbnail: String
) : ImageProjection
