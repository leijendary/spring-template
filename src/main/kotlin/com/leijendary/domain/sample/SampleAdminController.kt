package com.leijendary.domain.sample

import com.leijendary.domain.image.ImageRequest
import com.leijendary.model.QueryRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/samples/admin")
@Tag(name = "Sample Admin")
class SampleAdminController(private val sampleService: SampleService) {
    @GetMapping
    @Operation(
        summary = "List of all sample records in a paginated result.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["sample.admin.read"])]
    )
    fun page(queryRequest: QueryRequest, pageable: Pageable): Page<SampleResponse> {
        return sampleService.page(queryRequest, pageable)
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Saves a sample record into the database.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["sample.admin.write"])]
    )
    fun create(@Valid @RequestBody request: SampleRequest): SampleDetailResponse {
        return sampleService.create(request)
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Retrieves the translated sample record from the database.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["sample.admin.read"])]
    )
    fun get(@PathVariable id: String): SampleDetailResponse {
        return sampleService.get(id, false)
    }

    @PutMapping("{id}")
    @Operation(
        summary = "Updates the sample record into the database.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["sample.admin.write"])]
    )
    fun update(@PathVariable id: String, @Valid @RequestBody request: SampleRequest): SampleDetailResponse {
        return sampleService.update(id, request)
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Removes the sample record from the database.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["sample.admin.write"])]
    )
    fun delete(@PathVariable id: String) {
        sampleService.delete(id)
    }

    @PostMapping("{id}/image")
    @Operation(
        summary = "Validates and applies the image to the sample entity. Each request field is the image name.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["sample.admin.image.write"])]
    )
    fun saveImage(@PathVariable id: String, @Valid @RequestBody request: ImageRequest) {
        sampleService.saveImage(id, request)
    }

    @DeleteMapping("{id}/image")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Deletes the image from the sample entity. This will not actually delete the image.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["sample.admin.image.write"])]
    )
    fun deleteImage(@PathVariable id: String) {
        sampleService.deleteImage(id)
    }
}
