package com.leijendary.domain.sample

import com.leijendary.extension.elapsedTime
import com.leijendary.model.Page
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.*
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping("/api/v1/samples/admin")
@Tag(name = "Sample Admin")
class SampleAdminController(
    private val sampleSearchService: SampleSearchService,
    private val sampleService: SampleService
) {
    @GetMapping
    @Operation(
        summary = """
           List of all sample records in a cursor-based result. Use createdAt and id in the next request to get 
           the next page. This is a faster way to do pagination since it does not use limit offset, but rather
           uses the index to get the next page of the result.
        """
    )
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

    @PostMapping("reindex")
    @Operation(summary = "Reindex all objects to elasticsearch.")
    fun reindex(): String {
        var count: Int
        val time = measureTimeMillis {
            count = sampleSearchService.reindex()
        }
        val elapsed = time.elapsedTime()

        return "Re-indexed $count records to Sample Search, completed in $elapsed"
    }
}
