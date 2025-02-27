package com.leijendary.domain.image

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/images")
@Tag(name = "Image")
class ImageController(private val imageService: ImageService) {
    @PostMapping("url")
    @Operation(
        description = "Create an upload url based on the file name. Return the signed URL to use for uploading.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["image.write"])]
    )
    fun createUrl(@Valid @RequestBody request: ImageCreateUrlRequest): ImageCreateUrlResponse {
        return imageService.createUploadUrl(request)
    }

    @PostMapping("validate")
    @Operation(
        description = "Validate that the image is uploaded to the storage service and tag the image as validated.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["image.write"])]
    )
    fun validate(@Valid @RequestBody request: ImageValidateRequest): ImageValidateResponse {
        return imageService.validate(request.name)
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    @Operation(
        description = "Delete the image based on the name.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["image.write"])]
    )
    fun delete(@Valid @RequestBody request: ImageDeleteRequest) {
        imageService.delete(request.name)
    }
}
