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
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.*
import java.lang.Thread.sleep
import java.math.BigDecimal
import java.security.SecureRandom
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.Locale.getDefault
import kotlin.math.abs
import kotlin.math.ceil

class SampleSearchRestTest(
    @Autowired
    private val mockMvc: MockMvc,

    @Autowired
    private val messageSource: MessageSource
) : ApplicationTests() {
    private val sampleUrl = "/api/v1/samples"
    private val url = "/api/v1/samples/search"
    private val random = SecureRandom()
    private val userId = "junit-testing"
    private val scopeCreate = "urn:sample:create:v1"
    private val scopeDelete = "urn:sample:delete:v1"
    private val symbols = DecimalFormatSymbols(Locale.US)
    private val decimalFormat = DecimalFormat("0.0#", symbols)
    private val languages = arrayOf("en", "jp")
    private val listTotal = 21
    private val listSize = 10
    private val detailMemberSize = 7
    private val detailMetaSize = 3
    private val detailLinksSize = 1
    private val listMemberSize = 7
    private val listMetaSize = 4
    private val metaPageSize = 5

    @Test
    fun `Sample Search Page - Should return the search page based on the limit and query`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val requests = (1..listTotal).map { createRequest(suffix) }
        requests.forEach {
            mockMvc
                .post(sampleUrl) {
                    content = it.toJson()
                    contentType = APPLICATION_JSON
                    header(HEADER_USER_ID, userId)
                    header(HEADER_SCOPE, scopeCreate)
                }
                .andReturn()
                .response
                .contentAsString
                .toClass(DataResponse::class)
                .data!!
                .toClass(SampleResponse::class)
        }

        sleep(2000)

        val size = requests.size
        val totalPages = ceil(size.toDouble() / listSize.toDouble()).toInt()
        val lastPage = totalPages - 1

        languages.forEach { language ->
            var page = 0
            var nextIndex = size - 1

            while (nextIndex > 0) {
                val pageUri = "$url?query=$suffix&limit=$listSize&page=$page&size=$listSize&sort=createdAt,DESC"
                val selfUri = "$url?query=$suffix&limit=$listSize&page=$page&size=$listSize&sort=createdAt,DESC"
                val lastUri = "$url?query=$suffix&limit=$listSize&page=$lastPage&size=$listSize&sort=createdAt,DESC"
                val pageSize = if (nextIndex > listSize) listSize else nextIndex
                val linkSize = if (page in arrayOf(0, lastPage)) 3 else 4

                mockMvc
                    .get(pageUri) {
                        contentType = APPLICATION_JSON
                        header(ACCEPT_LANGUAGE, language)
                    }
                    .andExpect {
                        status { isOk() }
                        content { contentType(APPLICATION_JSON) }

                        jsonPath("$.data") { isArray() }
                        jsonPath("$.data.length()") { value(listSize) }
                        jsonPath("$.meta") { isMap() }
                        jsonPath("$.meta.length()") { value(listMetaSize) }
                        jsonPath("$.meta.traceId") { isNotEmpty() }
                        jsonPath("$.meta.timestamp") { isNumber() }
                        jsonPath("$.meta.page") { isMap() }
                        jsonPath("$.meta.page.length()") { value(metaPageSize) }
                        jsonPath("$.meta.page.numberOfElements") { value(pageSize) }
                        jsonPath("$.meta.page.totalPages") { value(totalPages) }
                        jsonPath("$.meta.page.totalElements") { value(size) }
                        jsonPath("$.meta.page.size") { value(listSize) }
                        jsonPath("$.meta.page.number") { value(page) }
                        jsonPath("$.meta.status") { value(OK.value()) }
                        jsonPath("$.links") { isMap() }
                        jsonPath("$.links.length()") { value(linkSize) }
                        jsonPath("$.links.self") { value(selfUri) }
                        jsonPath("$.links.last") { value(lastUri) }

                        if (page > 0) {
                            val previousUri =
                                "$url?query=$suffix&limit=$listSize&page=${page - 1}&size=$listSize&sort=createdAt,DESC"

                            jsonPath("$.links.previous") { value(previousUri) }
                        }

                        if (page < lastPage) {
                            val nextUri =
                                "$url?query=$suffix&limit=$listSize&page=${page + 1}&size=$listSize&sort=createdAt,DESC"

                            jsonPath("$.links.next") { value(nextUri) }
                        }


                        val lastIndex = nextIndex - pageSize + 1

                        for ((listIndex, i) in (nextIndex downTo lastIndex).withIndex()) {
                            jsonPath("$.data[$listIndex].length()") { value(listMemberSize) }

                            assertResponse(this, "$.data[$listIndex]", requests[i], language)

                            nextIndex--
                        }

                        page++
                    }
            }
        }
    }

    @Test
    fun `Sample Search Get - Should return the created search record`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val request = createRequest(suffix)
        val createResponse = mockMvc
            .post(sampleUrl) {
                content = request.toJson()
                contentType = APPLICATION_JSON
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

        sleep(2000)

        languages.forEach { language ->
            mockMvc
                .get(uri) {
                    contentType = APPLICATION_JSON
                    header(ACCEPT_LANGUAGE, language)
                }
                .andExpect {
                    status { isOk() }
                    content { contentType(APPLICATION_JSON) }

                    jsonPath("$.data") {
                        isNotEmpty()
                        jsonPath("$.data.length()") { value(detailMemberSize) }
                        jsonPath("$.meta") { isMap() }
                        jsonPath("$.meta.length()") { value(detailMetaSize) }
                        jsonPath("$.meta.traceId") { isNotEmpty() }
                        jsonPath("$.meta.timestamp") { isNumber() }
                        jsonPath("$.meta.status") { value(OK.value()) }
                        jsonPath("$.links") { isMap() }
                        jsonPath("$.links.length()") { value(detailLinksSize) }
                        jsonPath("$.links.self") { value(uri) }
                    }

                    assertResponse(this, "$.data", request, language)
                }
        }
    }

    @Test
    fun `Sample Delete - Should return empty then 404 after`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val request = createRequest(suffix)
        val createResponse = mockMvc
            .post(sampleUrl) {
                content = request.toJson()
                contentType = APPLICATION_JSON
                header(HEADER_USER_ID, userId)
                header(HEADER_SCOPE, scopeCreate)
            }
            .andReturn()
            .response
            .contentAsString
            .toClass(DataResponse::class)
            .data!!
            .toClass(SampleResponse::class)
        val sampleUri = "$sampleUrl/${createResponse.id}"

        sleep(2000)

        mockMvc
            .delete(sampleUri) {
                header(HEADER_USER_ID, userId)
                header(HEADER_SCOPE, scopeDelete)
            }
            .andExpect {
                status { isNoContent() }

                jsonPath("$") { doesNotExist() }
            }

        sleep(1000)

        val args = arrayOf("search.SampleSearch.id", createResponse.id)
        val message = messageSource.getMessage("error.resource.notFound", args, getDefault())
        val uri = "$url/${createResponse.id}"

        sleep(2000)

        mockMvc
            .get(uri)
            .andExpect {
                status { isNotFound() }

                jsonPath("$.errors") {
                    isArray()
                    jsonPath("$.errors.length()") { value(1) }
                    jsonPath("$.errors[0].source") { isArray() }
                    jsonPath("$.errors[0].source.length()") { value(3) }
                    jsonPath("$.errors[0].source[0]") { value("search") }
                    jsonPath("$.errors[0].source[1]") { value("SampleSearch") }
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
        language: String
    ) {
        val translated = request.translations!!.first { it.language == language }

        with(matchersDsl) {
            jsonPath("$path.id") { isNotEmpty() }
            jsonPath("$path.column1") { value(request.field1!!) }
            jsonPath("$path.column2") { value(request.field2!!) }
            jsonPath("$path.amount") { value(decimalFormat.format(request.amount!!)) }
            jsonPath("$path.name") { value(translated.name!!) }
            jsonPath("$path.description") { value(translated.description!!) }
            jsonPath("$path.createdAt") { isNumber() }
        }
    }
}