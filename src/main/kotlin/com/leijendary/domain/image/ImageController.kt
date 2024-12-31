package com.leijendary.domain.image

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/images")
@Tag(name = "Image")
class ImageController(private val imageService: ImageService) {
    @PostMapping("url")
    @Operation(
        summary = "Create an upload url based on the file name. Return the signed URL to use for uploading.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["image.write"])]
    )
    fun createUrl(@Valid @RequestBody request: ImageCreateUrlRequest): ImageCreateUrlResponse {
        return imageService.createUploadUrl(request)
    }

    @PostMapping("validate")
    @Operation(
        summary = "Validate that the image is uploaded to the storage service and tag the image as validated.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["image.write"])]
    )
    fun validate(@Valid @RequestBody request: ImageValidateRequest): ImageValidateResponse {
        return imageService.validate(request.name)
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Delete the image based on the name.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["image.write"])]
    )
    fun delete(@Valid @RequestBody request: ImageDeleteRequest) {
        imageService.delete(request.name)
    }
}
