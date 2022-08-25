package com.leijendary.spring.template.api.v1.rest

import com.leijendary.spring.template.api.v1.data.SampleSearchResponse
import com.leijendary.spring.template.api.v1.search.SampleSearch
import com.leijendary.spring.template.api.v1.service.SampleTableService
import com.leijendary.spring.template.core.data.DataResponse
import com.leijendary.spring.template.core.data.QueryRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping("/api/v1/samples/search")
@Tag(name = "Sample Search")
class SampleSearchRest(
    private val sampleSearch: SampleSearch,
    private val sampleTableService: SampleTableService
) {
    @GetMapping
    @Operation(summary = "List all the objects based on the query parameter")
    fun list(queryRequest: QueryRequest, pageable: Pageable): DataResponse<List<SampleSearchResponse>> {
        val page = sampleSearch.page(queryRequest, pageable)

        return DataResponse.builder<List<SampleSearchResponse>>()
            .data(page.content)
            .meta(page)
            .links(page)
            .build()
    }

    @GetMapping("{id}")
    @Operation(summary = "Get the specific object using the ID in elasticsearch")
    fun get(@PathVariable id: UUID): DataResponse<SampleSearchResponse> {
        val sampleResponse = sampleSearch.get(id)

        return DataResponse.builder<SampleSearchResponse>()
            .data(sampleResponse)
            .build()
    }

    @PostMapping("reindex")
    @Operation(summary = "Reindex all objects")
    fun reindex(): String {
        var count: Int
        val time = measureTimeMillis {
            count = sampleTableService.reindex()
        }

        return "Re-indexed $count records to Sample Search, completed in ${time}ms"
    }
}