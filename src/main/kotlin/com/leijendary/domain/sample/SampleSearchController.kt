package com.leijendary.domain.sample

import com.leijendary.model.Page
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/samples/search")
@Tag(name = "Sample Search")
class SampleSearchController(private val sampleSearchService: SampleSearchService) {
    @GetMapping
    @Operation(summary = "List all the objects based on the query parameter")
    fun list(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleList> {
        return sampleSearchService.page(queryRequest, pageRequest)
    }

    @GetMapping("{id}")
    @Operation(summary = "Get the specific object using the ID in elasticsearch")
    fun get(@PathVariable id: Long): SampleDetail {
        return sampleSearchService.get(id)
    }
}
