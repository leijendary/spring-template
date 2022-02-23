package com.leijendary.spring.boot.template.api.v1.controller

import com.leijendary.spring.boot.template.api.v1.data.SampleRequest
import com.leijendary.spring.boot.template.api.v1.data.SampleResponse
import com.leijendary.spring.boot.template.api.v1.service.SampleTableService
import com.leijendary.spring.boot.template.api.v1.service.SampleTableService.Companion.CACHE_NAME
import com.leijendary.spring.boot.template.client.SampleClient
import com.leijendary.spring.boot.template.core.data.DataResponse
import com.leijendary.spring.boot.template.core.data.QueryRequest
import com.leijendary.spring.boot.template.core.extension.setLocation
import com.leijendary.spring.boot.template.core.util.RequestContext.language
import com.leijendary.spring.boot.template.core.util.RequestContext.locale
import com.leijendary.spring.boot.template.core.util.RequestContext.now
import com.leijendary.spring.boot.template.core.util.RequestContext.timeZone
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.MediaType.TEXT_HTML_VALUE
import org.springframework.http.MediaType.TEXT_PLAIN_VALUE
import org.springframework.web.bind.annotation.*
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.TextStyle.FULL
import java.util.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

/**
 * This is an example of a controller that will be created in microservices.
 *
 * There are 3 parts of the [RequestMapping] url that we need to take note of:
 *      1. The api prefix ("api")
 *      2. The version ("v1")
 *      3. The parent path of this API ("/") which can be anything that this specific controller should be doing.
 *
 * Since this microservice uses a context path, the result of the url should be
 * "/sample/api/v1"
 *
 * The url paths should be in kebab-case except for the query parameters, body,
 * and other URL parts in which they should be in camelCase.
 *
 * For headers, I would recommend that the Header keys should be in
 * Pascal-Kebab-Case
 */
@RestController
@RequestMapping("/api/v1/samples")
@Tag(name = "Sample")
class SampleController(private val sampleClient: SampleClient, private val sampleTableService: SampleTableService) {

    /**
     * This is a sample RequestMapping (Only GET method, that is why I used
     * [GetMapping])
     *
     * @param pageable The page request. Since this API has pageable results, it is
     * recommended that the request parameters contains
     * [Pageable]
     */
    @GetMapping
    @Operation(summary = "Sample implementation of swagger in a api")
    fun list(queryRequest: QueryRequest, pageable: Pageable): DataResponse<List<SampleResponse>> {
        val page: Page<SampleResponse> = sampleTableService.page(queryRequest, pageable)

        return DataResponse.builder<List<SampleResponse>>()
            .data(page.content)
            .meta(page)
            .links(page)
            .build()
    }

    @PostMapping
    @CachePut(value = [CACHE_NAME], key = "#result.data.id")
    @ResponseStatus(CREATED)
    @Operation(summary = "Saves a sample record into the database")
    fun create(
        @Valid @RequestBody request: SampleRequest,
        httpServletResponse: HttpServletResponse
    ): DataResponse<SampleResponse> {
        val sampleResponse: SampleResponse = sampleTableService.create(request)
        val response = DataResponse.builder<SampleResponse>()
            .data(sampleResponse)
            .status(CREATED)
            .build()

        httpServletResponse.setLocation(sampleResponse.id)

        return response
    }

    @GetMapping("{id}")
    @Cacheable(value = [CACHE_NAME], key = "#id")
    @Operation(summary = "Retrieves the sample record from the database")
    fun get(@PathVariable id: UUID): DataResponse<SampleResponse> {
        val sampleResponse: SampleResponse = sampleTableService.get(id)

        return DataResponse.builder<SampleResponse>()
            .data(sampleResponse)
            .build()
    }

    @PutMapping("{id}")
    @CachePut(value = [CACHE_NAME], key = "#result.data.id")
    @Operation(summary = "Updates the sample record into the database")
    fun update(@PathVariable id: UUID, @Valid @RequestBody request: SampleRequest): DataResponse<SampleResponse> {
        val sampleResponse: SampleResponse = sampleTableService.update(id, request)

        return DataResponse.builder<SampleResponse>()
            .data(sampleResponse)
            .build()
    }

    @DeleteMapping("{id}")
    @CacheEvict(value = [CACHE_NAME], key = "#id")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Removes the sample record from the database")
    fun delete(@PathVariable id: UUID) {
        sampleTableService.delete(id)
    }

    @GetMapping(value = ["client"], produces = [TEXT_HTML_VALUE])
    fun client(): String {
        return sampleClient.homepage()
    }

    @GetMapping(value = ["timezone"], produces = [TEXT_PLAIN_VALUE])
    fun timezone(): String {
        val timeZone: TimeZone = timeZone
        val zoneId: ZoneId = timeZone.toZoneId()
        val displayName: String = zoneId.getDisplayName(FULL, locale)
        val id: String = zoneId.id

        return String.format("%s %s", displayName, id)
    }

    @GetMapping(value = ["locale"], produces = [TEXT_PLAIN_VALUE])
    fun locale(): String {
        return locale.toString()
    }

    @GetMapping(value = ["language"], produces = [TEXT_PLAIN_VALUE])
    fun language(): String {
        return language
    }

    @GetMapping("timestamp")
    fun timestamp(): Map<String, OffsetDateTime> {
        val map: HashMap<String, OffsetDateTime> = HashMap<String, OffsetDateTime>()
        map["current"] = now

        return map
    }
}