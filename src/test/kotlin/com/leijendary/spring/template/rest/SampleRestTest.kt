package com.leijendary.spring.template.rest

import com.leijendary.spring.template.ApplicationTests
import com.leijendary.spring.template.api.v1.data.SampleRequest
import com.leijendary.spring.template.api.v1.data.SampleResponse
import com.leijendary.spring.template.api.v1.data.SampleTranslationRequest
import com.leijendary.spring.template.core.data.DataResponse
import com.leijendary.spring.template.core.extension.AnyUtil.toJson
import com.leijendary.spring.template.core.extension.scaled
import com.leijendary.spring.template.core.extension.toClass
import com.leijendary.spring.template.core.util.HEADER_SCOPE
import com.leijendary.spring.template.core.util.HEADER_USER_ID
import org.apache.commons.codec.net.URLCodec
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus.*
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.*
import org.springframework.web.util.UriComponentsBuilder
import java.lang.Thread.sleep
import java.math.BigDecimal
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8
import java.security.SecureRandom
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.Locale.getDefault
import kotlin.math.abs

class SampleRestTest(
    @Autowired
    private val mockMvc: MockMvc,

    @Autowired
    private val messageSource: MessageSource
) : ApplicationTests() {
    private val url = "/api/v1/samples"
    private val random = SecureRandom()
    private val userId = "junit-testing"
    private val scopeList = "urn:sample:list:v1"
    private val scopeCreate = "urn:sample:create:v1"
    private val scopeGet = "urn:sample:get:v1"
    private val scopeUpdate = "urn:sample:update:v1"
    private val scopeDelete = "urn:sample:delete:v1"
    private val symbols = DecimalFormatSymbols(Locale.US)
    private val decimalFormat = DecimalFormat("0.0#", symbols)
    private val listSize = 21
    private val listLimit = 10
    private val detailMemberSize = 10
    private val detailMetaSize = 3
    private val detailLinksSize = 1
    private val listMemberSize = 10
    private val listMetaSize = 4
    private val metaSeekSize = 5
    private val listLinksSize = 2

    @Test
    fun `Sample Seek - Should return the list based on the limit and next token`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val requests = (1..listSize).map { createRequest(suffix) }
        requests.forEach {
            mockMvc
                .post(url) {
                    contentType = APPLICATION_JSON
                    content = it.toJson()
                    header(HEADER_USER_ID, userId)
                    header(HEADER_SCOPE, scopeCreate)
                }
                .andExpect {
                    status { isCreated() }
                    content { contentType(APPLICATION_JSON) }
                }
                .andReturn()
                .response
                .contentAsString
                .toClass(DataResponse::class)
                .data!!
                .toClass(SampleResponse::class)
        }

        val query = "junit test $suffix"
        val size = requests.size
        var nextIndex = size - 1
        var nextToken = ""
        val uri = UriComponentsBuilder.fromUriString(url)
            .queryParam("query", query)
            .queryParam("limit", listLimit)

        while (nextIndex > 0) {
            val requestUri = uri
                .cloneBuilder()
                .replaceQueryParam("nextToken", nextToken)
                .build()
                .toUriString()
            val selfUri = uri
                .cloneBuilder()
                .replaceQueryParam("query", URLEncoder.encode(query, UTF_8).replace("+", "%20"))
                .replaceQueryParam("limit", listLimit)
                .replaceQueryParam("nextToken", URLCodec().encode(nextToken))
                .build()
                .toUriString()
            val seekSize = if (nextIndex > listLimit) listLimit else nextIndex
            val response = mockMvc
                .get(requestUri) {
                    header(HEADER_USER_ID, userId)
                    header(HEADER_SCOPE, scopeList)
                }
                .andExpect {
                    status { isOk() }
                    content { contentType(APPLICATION_JSON) }

                    jsonPath("$.data") { isArray() }
                    jsonPath("$.data.length()") { value(listLimit) }
                    jsonPath("$.meta") { isMap() }
                    jsonPath("$.meta.length()") { value(listMetaSize) }
                    jsonPath("$.meta.traceId") { isNotEmpty() }
                    jsonPath("$.meta.timestamp") { isNumber() }
                    jsonPath("$.meta.seek") { isMap() }
                    jsonPath("$.meta.seek.length()") { value(metaSeekSize) }
                    jsonPath("$.meta.seek.size") { value(seekSize) }
                    jsonPath("$.meta.seek.limit") { value(listLimit) }
                    jsonPath("$.meta.status") { value(OK.value()) }
                    jsonPath("$.links") { isMap() }
                    jsonPath("$.links.length()") { value(listLinksSize) }
                    jsonPath("$.links.self") { value(selfUri) }

                    val lastIndex = nextIndex - seekSize + 1

                    for ((listIndex, i) in (nextIndex downTo lastIndex).withIndex()) {
                        jsonPath("$.data[$listIndex].length()") { value(listMemberSize) }

                        assertResponse(
                            this,
                            "$.data[$listIndex]",
                            requests[i],
                            false
                        )

                        nextIndex--
                    }
                }
                .andReturn()
                .response
                .contentAsString
                .toClass(DataResponse::class)

            val meta = response.meta
            val seek = meta["seek"]?.let { it as MutableMap<*, *> }
            val links = response.links

            nextToken = if (nextIndex >= 0) {
                links
                    ?.let { it["next"] }
                    .run { assert(this != null) }

                seek
                    ?.let { it["nextToken"] }
                    ?.let { it as String }
                    ?.let {
                        assert(it.isNotEmpty())

                        it
                    }
                    ?: ""
            } else {
                links
                    ?.let { it["next"] }
                    .run { assert(this == null) }
                seek
                    ?.let { it["nextToken"] }
                    .let { it as String }
                    .run { assert(isEmpty()) }

                ""
            }
        }
    }

    @Test
    fun `Sample Create - Should create multiple and return created records`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val request = createRequest(suffix)

        mockMvc
            .post(url) {
                contentType = APPLICATION_JSON
                content = request.toJson()
                header(HEADER_USER_ID, userId)
                header(HEADER_SCOPE, scopeCreate)
            }
            .andExpect {
                status { isCreated() }
                content { contentType(APPLICATION_JSON) }

                jsonPath("$.data") { isNotEmpty() }
                jsonPath("$.data.length()") { value(detailMemberSize) }
                jsonPath("$.meta") { isMap() }
                jsonPath("$.meta.length()") { value(detailMetaSize) }
                jsonPath("$.meta.traceId") { isNotEmpty() }
                jsonPath("$.meta.timestamp") { isNumber() }
                jsonPath("$.meta.status") { value(CREATED.value()) }
                jsonPath("$.links") { isMap() }
                jsonPath("$.links.length()") { value(detailLinksSize) }
                jsonPath("$.links.self") { value(url) }

                assertResponse(this, "$.data", request)

                for (translation in request.translations!!) {
                    val translationPath = languagePath("$.data.translations", translation.language!!)

                    assertTranslations(this, translationPath, translation)
                }
            }
    }

    @Test
    fun `Sample Get - Should return the created record`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val request = createRequest(suffix)
        val createResponse = mockMvc
            .post(url) {
                contentType = APPLICATION_JSON
                content = request.toJson()
                header(HEADER_USER_ID, userId)
                header(HEADER_SCOPE, scopeCreate)
            }
            .andReturn()
            .response
            .contentAsString
            .toClass(DataResponse::class)
            .data!!
            .toClass(SampleResponse::class)
        val uri = "$url/${createResponse.id}"

        mockMvc
            .get(uri) {
                contentType = APPLICATION_JSON
                content = request.toJson()
                header(HEADER_USER_ID, userId)
                header(HEADER_SCOPE, scopeGet)
            }
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }

                jsonPath("$.data") { isNotEmpty() }
                jsonPath("$.data.length()") { value(detailMemberSize) }
                jsonPath("$.meta") { isMap() }
                jsonPath("$.meta.length()") { value(detailMetaSize) }
                jsonPath("$.meta.traceId") { isNotEmpty() }
                jsonPath("$.meta.timestamp") { isNumber() }
                jsonPath("$.meta.status") { value(OK.value()) }
                jsonPath("$.links") { isMap() }
                jsonPath("$.links.length()") { value(detailLinksSize) }
                jsonPath("$.links.self") { value(uri) }

                assertResponse(this, "$.data", request)


                for (translation in request.translations!!) {
                    val translationPath = languagePath("$.data.translations", translation.language!!)

                    assertTranslations(this, translationPath, translation)
                }
            }
    }

    @Test
    fun `Sample Update - Should return the updated record`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val request = createRequest(suffix)
        val createResponse = mockMvc
            .post(url) {
                contentType = APPLICATION_JSON
                content = request.toJson()
                header(HEADER_USER_ID, userId)
                header(HEADER_SCOPE, scopeCreate)
            }
            .andReturn()
            .response
            .contentAsString
            .toClass(DataResponse::class)
            .data!!
            .toClass(SampleResponse::class)
        val uri = "$url/${createResponse.id}"
        val dutch = SampleTranslationRequest(
            "Test Dutch - Updated",
            "Test Dutch Description - Updated"
        )
        dutch.language = "nl"
        dutch.ordinal = 1

        val english = SampleTranslationRequest(
            "Test English - Updated",
            "Test English Description - Updated"
        )
        english.language = "en"
        english.ordinal = 2

        val german = SampleTranslationRequest(
            "Test German - Updated",
            "Test German Description - Updated"
        )
        german.language = "de"
        german.ordinal = 3

        val french = SampleTranslationRequest(
            "Test French - Updated",
            "Test French Description - Updated"
        )
        french.language = "fr"
        french.ordinal = 4

        val updatedTranslations = listOf(dutch, english, german, french)
        val updatedRequest = SampleRequest(
            "${request.field1} - Updated",
            request.field2!!.plus(200),
            request.amount!!.add(BigDecimal.valueOf(200)),
            updatedTranslations
        )

        mockMvc
            .put(uri) {
                contentType = APPLICATION_JSON
                content = updatedRequest.toJson()
                header(HEADER_USER_ID, userId)
                header(HEADER_SCOPE, scopeUpdate)
            }
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }

                jsonPath("$.data") { isNotEmpty() }
                jsonPath("$.data.length()") { value(detailMemberSize) }
                jsonPath("$.meta") { isMap() }
                jsonPath("$.meta.length()") { value(detailMetaSize) }
                jsonPath("$.meta.traceId") { isNotEmpty() }
                jsonPath("$.meta.timestamp") { isNumber() }
                jsonPath("$.meta.status") { value(OK.value()) }
                jsonPath("$.links") { isMap() }
                jsonPath("$.links.length()") { value(detailLinksSize) }
                jsonPath("$.links.self") { value(uri) }

                assertResponse(this, "$.data", updatedRequest)

                for (translation in updatedRequest.translations!!) {
                    val translationPath = languagePath("$.data.translations", translation.language!!)

                    assertTranslations(this, translationPath, translation)
                }
            }
    }

    @Test
    fun `Sample Delete - Should return empty then 404 after`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val request = createRequest(suffix)
        val createResponse = mockMvc
            .post(url) {
                contentType = APPLICATION_JSON
                content = request.toJson()
                header(HEADER_USER_ID, userId)
                header(HEADER_SCOPE, scopeCreate)
            }
            .andReturn()
            .response
            .contentAsString
            .toClass(DataResponse::class)
            .data!!
            .toClass(SampleResponse::class)
        val uri = "$url/${createResponse.id}"

        mockMvc
            .delete(uri) {
                header(HEADER_USER_ID, userId)
                header(HEADER_SCOPE, scopeDelete)
            }
            .andExpect {
                status { isNoContent() }

                jsonPath("$") { doesNotExist() }
            }

        sleep(1000)

        val args = arrayOf("data.SampleTable.id", createResponse.id)
        val message = messageSource.getMessage("error.resource.notFound", args, getDefault())

        mockMvc
            .get(uri) {
                header(HEADER_USER_ID, userId)
                header(HEADER_SCOPE, scopeGet)
            }
            .andExpect {
                status { isNotFound() }
                content { contentType(APPLICATION_JSON) }

                jsonPath("$.errors") { isArray() }
                jsonPath("$.errors.length()") { value(1) }
                jsonPath("$.errors[0].source") { isArray() }
                jsonPath("$.errors[0].source.length()") { value(3) }
                jsonPath("$.errors[0].source[0]") { value("data") }
                jsonPath("$.errors[0].source[1]") { value("SampleTable") }
                jsonPath("$.errors[0].source[2]") { value("id") }
                jsonPath("$.errors[0].code") { value("error.resource.notFound") }
                jsonPath("$.errors[0].message") { value(message) }
                jsonPath("$.meta") { isMap() }
                jsonPath("$.meta.length()") { value(detailMetaSize) }
                jsonPath("$.meta.traceId") { isNotEmpty() }
                jsonPath("$.meta.timestamp") { isNumber() }
                jsonPath("$.meta.status") { value(NOT_FOUND.value()) }
                jsonPath("$.links") { isMap() }
                jsonPath("$.links.length()") { value(detailLinksSize) }
                jsonPath("$.links.self") { value(uri) }
            }
    }

    private fun createRequest(suffix: String): SampleRequest {
        val field1 = "JUnit Test $suffix - ${abs(random.nextInt())}"
        val field2 = abs(random.nextLong())
        val amount = abs(random.nextDouble())
            .let { if (it == 0.00) "0.01".toDouble() else it }
            .toBigDecimal()
            .multiply(BigDecimal.valueOf(100000))
            .scaled()

        val englishTranslationName = "Test English - $suffix"
        val englishTranslationDescription = "Test English Description - $suffix"
        val englishTranslationLanguage = "en"
        val englishTranslationOrdinal = 1
        val englishRequest = SampleTranslationRequest(englishTranslationName, englishTranslationDescription)
        englishRequest.language = englishTranslationLanguage
        englishRequest.ordinal = englishTranslationOrdinal

        val japaneseTranslationName = "Test Japanese - $suffix"
        val japaneseTranslationDescription = "Test Japanese Description - $suffix"
        val japaneseTranslationLanguage = "jp"
        val japaneseTranslationOrdinal = 2
        val japaneseRequest = SampleTranslationRequest(japaneseTranslationName, japaneseTranslationDescription)
        japaneseRequest.language = japaneseTranslationLanguage
        japaneseRequest.ordinal = japaneseTranslationOrdinal

        return SampleRequest(field1, field2, amount, arrayListOf(englishRequest, japaneseRequest))
    }

    private fun assertResponse(
        matchersDsl: MockMvcResultMatchersDsl,
        path: String,
        request: SampleRequest,
        includeTranslations: Boolean = true
    ) {
        with(matchersDsl) {
            jsonPath("$path.id") { isNotEmpty() }
            jsonPath("$path.column1") { value(request.field1!!) }
            jsonPath("$path.column2") { value(request.field2!!) }
            jsonPath("$path.amount") { value(decimalFormat.format(request.amount!!)) }
            jsonPath("$path.createdAt") { isNumber() }
            jsonPath("$path.createdBy") { value(userId) }
            jsonPath("$path.lastModifiedAt") { isNumber() }
            jsonPath("$path.lastModifiedBy") { value(userId) }

            if (includeTranslations) {
                matchersDsl.jsonPath("$path.translations") { isArray() }
                matchersDsl.jsonPath("$path.translations.length()") { value(request.translations!!.size) }
            }
        }
    }

    private fun assertTranslations(
        matchersDsl: MockMvcResultMatchersDsl,
        path: String,
        translation: SampleTranslationRequest
    ) {
        with(matchersDsl) {
            jsonPath("$path.name") { value(translation.name!!) }
            jsonPath("$path.description") { value(translation.description!!) }
            jsonPath("$path.ordinal") { value(translation.ordinal) }
        }
    }

    private fun languagePath(path: String, language: String): String = "$path[?(@.language=='$language')]"
}