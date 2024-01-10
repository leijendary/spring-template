package com.leijendary.domain.sample

import com.leijendary.extension.elapsedTime
import com.leijendary.model.Page
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping("/api/v1/samples/search")
@Tag(name = "Sample Search")
class SampleSearchController(private val sampleSearchService: SampleSearchService) {
    @GetMapping
    @Operation(summary = "List all the objects based on the query parameter")
    fun list(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleList> {
        return sampleSearchService.page(queryRequest, pageRequest)
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
