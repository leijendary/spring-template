package com.leijendary.domain.sample

import com.leijendary.extension.elapsedTime
import com.leijendary.model.QueryRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping("v1/samples/search")
@Tag(name = "Sample Search")
class SampleSearchController(private val sampleSearchService: SampleSearchService) {
    @GetMapping
    @Operation(
        description = "List all the objects based on the query parameter",
        security = [SecurityRequirement(name = "oauth2", scopes = ["sample.search.read"])]
    )
    fun list(queryRequest: QueryRequest, pageable: Pageable): Page<SampleResponse> {
        return sampleSearchService.page(queryRequest, pageable)
    }

    @PostMapping("reindex")
    @Operation(
        description = "Reindex all objects to elasticsearch.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["sample.search.write"])]
    )
    fun reindex(): String {
        var count: Int
        val time = measureTimeMillis {
            count = sampleSearchService.reindex()
        }
        val elapsed = time.elapsedTime()

        return "Re-indexed $count records to Sample Search, completed in $elapsed"
    }
}
