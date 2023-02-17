package com.leijendary.spring.template.api.v1.rest

import com.leijendary.spring.template.api.v1.model.SampleRequest
import com.leijendary.spring.template.api.v1.model.SampleResponse
import com.leijendary.spring.template.api.v1.service.SampleTableService
import com.leijendary.spring.template.client.SampleClient
import com.leijendary.spring.template.core.model.QueryRequest
import com.leijendary.spring.template.core.model.Seekable
import com.leijendary.spring.template.core.util.RequestContext.language
import com.leijendary.spring.template.core.util.RequestContext.locale
import com.leijendary.spring.template.core.util.RequestContext.now
import com.leijendary.spring.template.core.util.RequestContext.timeZone
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.MediaType.TEXT_HTML_VALUE
import org.springframework.http.MediaType.TEXT_PLAIN_VALUE
import org.springframework.web.bind.annotation.*
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.TextStyle.FULL
import java.util.*

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
class SampleRest(private val sampleClient: SampleClient, private val sampleTableService: SampleTableService) {
    /**
     * This is a sample RequestMapping (Only GET method, that is why I used
     * [GetMapping])
     *
     * @param seekable The seek request. Since this API has seekable results, it is
     * recommended that the request parameters contains [Seekable]
     */
    @GetMapping
    @Operation(summary = "Sample implementation of swagger in a api")
    fun seek(queryRequest: QueryRequest, seekable: Seekable) = sampleTableService.seek(queryRequest, seekable)

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Saves a sample record into the database")
    fun create(@Valid @RequestBody request: SampleRequest) = sampleTableService.create(request)

    @GetMapping("{id}")
    @Operation(summary = "Retrieves the sample record from the database")
    fun get(@PathVariable id: UUID) = sampleTableService.get(id)

    @PutMapping("{id}")
    @Operation(summary = "Updates the sample record into the database")
    fun update(@PathVariable id: UUID, @Valid @RequestBody request: SampleRequest): SampleResponse {
        return sampleTableService.update(id, request)
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Removes the sample record from the database")
    fun delete(@PathVariable id: UUID) = sampleTableService.delete(id)

    @GetMapping(value = ["client"], produces = [TEXT_HTML_VALUE])
    fun client() = sampleClient.homepage()

    @GetMapping(value = ["timezone"], produces = [TEXT_PLAIN_VALUE])
    fun timezone(): String {
        val timeZone: TimeZone = timeZone
        val zoneId: ZoneId = timeZone.toZoneId()
        val displayName: String = zoneId.getDisplayName(FULL, locale)
        val id: String = zoneId.id

        return String.format("%s %s", displayName, id)
    }

    @GetMapping(value = ["locale"], produces = [TEXT_PLAIN_VALUE])
    fun locale() = locale.toString()

    @GetMapping(value = ["language"], produces = [TEXT_PLAIN_VALUE])
    fun language() = language

    @GetMapping("timestamp")
    fun timestamp(): Map<String, OffsetDateTime> {
        val map: HashMap<String, OffsetDateTime> = HashMap<String, OffsetDateTime>()
        map["current"] = now

        return map
    }
}
