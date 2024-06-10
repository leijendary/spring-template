package com.leijendary.domain.sample

import com.leijendary.domain.image.ImageRequest
import com.leijendary.model.Page
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/samples/admin")
@Tag(name = "Sample Admin")
class SampleAdminController(private val sampleService: SampleService) {
    @GetMapping
    @Operation(summary = "List of all sample records in a paginated result.")
    fun page(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleList> {
        return sampleService.page(queryRequest, pageRequest)
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Saves a sample record into the database.")
    fun create(@Valid @RequestBody request: SampleRequest): SampleDetail {
        return sampleService.create(request)
    }

    @GetMapping("{id}")
    @Operation(summary = "Retrieves the translated sample record from the database.")
    fun get(@PathVariable id: Long): SampleDetail {
        return sampleService.get(id, false)
    }

    @PutMapping("{id}")
    @Operation(summary = "Updates the sample record into the database.")
    fun update(
        @PathVariable id: Long,
        @RequestParam version: Int,
        @Valid @RequestBody request: SampleRequest
    ): SampleDetail {
        return sampleService.update(id, version, request)
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Removes the sample record from the database. This is a soft delete.")
    fun delete(@PathVariable id: Long, @RequestParam version: Int) {
        sampleService.delete(id, version)
    }

    @PostMapping("{id}/image")
    @Operation(summary = "Validates and applies the image to the sample entity. Each request field is the image name.")
    fun saveImage(@PathVariable id: Long, @Valid @RequestBody request: ImageRequest) {
        sampleService.saveImage(id, request)
    }

    @DeleteMapping("{id}/image")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Deletes the image from the sample entity. This will not actually delete the image.")
    fun deleteImage(@PathVariable id: Long) {
        sampleService.deleteImage(id)
    }
}
