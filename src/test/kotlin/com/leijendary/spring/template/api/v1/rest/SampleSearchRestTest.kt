package com.leijendary.spring.template.api.v1.rest

import com.leijendary.spring.template.ApplicationTest
import com.leijendary.spring.template.api.v1.data.SampleRequest
import com.leijendary.spring.template.api.v1.data.SampleResponse
import com.leijendary.spring.template.api.v1.data.SampleTranslationRequest
import com.leijendary.spring.template.core.extension.AnyUtil.toJson
import com.leijendary.spring.template.core.extension.scaled
import com.leijendary.spring.template.core.extension.toClass
import com.leijendary.spring.template.core.filter.HEADER_TRACE_ID
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
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
    private val messageSource: MessageSource
) : ApplicationTest() {
    private val sampleUrl = "/api/v1/samples"
    private val url = "/api/v1/samples/search"
    private val random = SecureRandom()
    private val symbols = DecimalFormatSymbols(Locale.US)
    private val decimalFormat = DecimalFormat("0.0#", symbols)
    private val languages = arrayOf("en", "jp")
    private val listTotal = 21
    private val listSize = 10
    private val detailMemberSize = 7
    private val listMemberSize = 7

    @Test
    @WithMockUser(username = "user-for-create", authorities = ["SCOPE_urn:sample:create:v1"])
    fun `Page should return the search page based on the limit and query`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val requests = (1..listTotal).map { createRequest(suffix) }
        requests.forEach {
            mockMvc
                .post(sampleUrl) {
                    contentType = APPLICATION_JSON
                    content = it.toJson()
                }
                .andReturn()
                .response
                .contentAsString
                .toClass(SampleResponse::class)
        }

        sleep(2000)

        val size = requests.size
        val totalPages = ceil(size.toDouble() / listSize.toDouble()).toInt()
        val direction = "desc"
        val field = "createdAt"
        val sort = Sort.by(Direction.valueOf(direction.uppercase()), field)

        languages.forEach { language ->
            var page = 0
            var nextIndex = size - 1

            while (nextIndex > 0) {
                val pageUri = "$url?query=$suffix&limit=$listSize&page=$page&size=$listSize&sort=$field,$direction"
                val pageSize = if (nextIndex > listSize) listSize else nextIndex

                mockMvc
                    .get(pageUri) {
                        contentType = APPLICATION_JSON
                        header(ACCEPT_LANGUAGE, language)
                    }
                    .andExpect {
                        status { isOk() }
                        header { exists(HEADER_TRACE_ID) }
                        content { contentType(APPLICATION_JSON) }

                        jsonPath("$.content") { isArray() }
                        jsonPath("$.content.length()") { value(listSize) }

                        assertPage(this, listSize, sort, page, pageSize, size, totalPages)

                        val lastIndex = nextIndex - pageSize + 1

                        for ((listIndex, i) in (nextIndex downTo lastIndex).withIndex()) {
                            jsonPath("$.content[$listIndex].length()") { value(listMemberSize) }

                            assertResponse(this, "$.content[$listIndex]", requests[i], language)

                            nextIndex--
                        }

                        page++
                    }
            }
        }
    }

    @Test
    @WithMockUser(username = "user-for-create", authorities = ["SCOPE_urn:sample:create:v1"])
    fun `Get should return the created search record`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val request = createRequest(suffix)
        val createResponse = mockMvc
            .post(sampleUrl) {
                contentType = APPLICATION_JSON
                content = request.toJson()
            }
            .andReturn()
            .response
            .contentAsString
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
                    header { exists(HEADER_TRACE_ID) }
                    content { contentType(APPLICATION_JSON) }

                    jsonPath("$") { isMap() }
                    jsonPath("$.length()") { value(detailMemberSize) }

                    assertResponse(this, "$", request, language)
                }
        }
    }

    @Test
    @WithMockUser(
        username = "user-for-create-and-delete",
        authorities = ["SCOPE_urn:sample:create:v1", "SCOPE_urn:sample:delete:v1"]
    )
    fun `Delete should return empty then 404 after`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val request = createRequest(suffix)
        val createResponse = mockMvc
            .post(sampleUrl) {
                contentType = APPLICATION_JSON
                content = request.toJson()
            }
            .andReturn()
            .response
            .contentAsString
            .toClass(SampleResponse::class)
        val sampleUri = "$sampleUrl/${createResponse.id}"

        sleep(2000)

        mockMvc
            .delete(sampleUri)
            .andExpect {
                status { isNoContent() }
                header { exists(HEADER_TRACE_ID) }

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
                header { exists(HEADER_TRACE_ID) }

                jsonPath("$") { isArray() }
                jsonPath("$.length()") { value(1) }
                jsonPath("$[0].source") { isArray() }
                jsonPath("$[0].source.length()") { value(3) }
                jsonPath("$[0].source[0]") { value("search") }
                jsonPath("$[0].source[1]") { value("SampleSearch") }
                jsonPath("$[0].source[2]") { value("id") }
                jsonPath("$[0].code") { value("error.resource.notFound") }
                jsonPath("$[0].message") { value(message) }
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
            jsonPath("$path.createdAt") { isString() }
        }
    }
}
