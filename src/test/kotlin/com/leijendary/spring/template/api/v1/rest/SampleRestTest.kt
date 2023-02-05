package com.leijendary.spring.template.api.v1.rest

import com.leijendary.spring.template.api.v1.model.SampleRequest
import com.leijendary.spring.template.api.v1.model.SampleResponse
import com.leijendary.spring.template.api.v1.model.SampleTranslationRequest
import com.leijendary.spring.template.core.extension.AnyUtil.toJson
import com.leijendary.spring.template.core.extension.scaled
import com.leijendary.spring.template.core.extension.toClass
import com.leijendary.spring.template.core.filter.HEADER_TRACE_ID
import com.leijendary.spring.template.core.model.Seek
import com.leijendary.spring.template.helper.AssertionHelper.assertSeek
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.MessageSource
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.*
import org.springframework.web.util.UriComponentsBuilder
import java.lang.Thread.sleep
import java.math.BigDecimal
import java.security.SecureRandom
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.Locale.getDefault
import kotlin.math.abs
import kotlin.math.ceil

@WebMvcTest(SampleRest::class)
class SampleRestTest {
    private val url = "/api/v1/samples"
    private val random = SecureRandom()
    private val symbols = DecimalFormatSymbols(Locale.US)
    private val decimalFormat = DecimalFormat("0.0#", symbols)
    private val listSize = 21
    private val listLimit = 10
    private val detailMemberSize = 10
    private val listMemberSize = 10
    private val translationMemberSize = 4

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var messageSource: MessageSource

    @Test
    @WithMockUser(
        username = "user-for-create-and-list",
        authorities = ["SCOPE_urn:sample:list:v1", "SCOPE_urn:sample:create:v1"]
    )
    fun `Seek should return the list based on the limit and next token`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val requests = (1..listSize).map { createRequest(suffix) }
        requests.forEach {
            mockMvc
                .post(url) {
                    contentType = APPLICATION_JSON
                    content = it.toJson()
                }
                .andExpect {
                    status { isCreated() }
                    header { exists(HEADER_TRACE_ID) }
                    content { contentType(APPLICATION_JSON) }
                }
                .andReturn()
                .response
                .contentAsString
                .toClass(SampleResponse::class)
        }

        val query = "junit test $suffix"
        val size = requests.size
        val totalPages = ceil(size.toDouble() / listLimit.toDouble()).toInt()
        var nextIndex = size - 1
        var nextToken = ""
        val direction = "desc"
        val field = "createdAt"
        val fields = arrayOf(field, "rowId")
        val sort = Sort.by(Direction.valueOf(direction.uppercase()), *fields)
        val uri = UriComponentsBuilder.fromUriString(url)
            .queryParam("query", query)
            .queryParam("limit", listLimit)
            .queryParam("sort", field)
            .queryParam("direction", direction)
        var page = 0

        while (nextIndex > 0) {
            val requestUri = uri
                .cloneBuilder()
                .replaceQueryParam("nextToken", nextToken)
                .build()
                .toUriString()
            val seekSize = if (nextIndex > listLimit) listLimit else nextIndex
            val response = mockMvc
                .get(requestUri)
                .andExpect {
                    status { isOk() }
                    header { exists(HEADER_TRACE_ID) }
                    content { contentType(APPLICATION_JSON) }

                    jsonPath("$.content") { isArray() }
                    jsonPath("$.content.length()") { value(listLimit) }

                    assertSeek(this, seekSize, listLimit, sort, page, totalPages)

                    val lastIndex = nextIndex - seekSize + 1

                    for ((listIndex, i) in (nextIndex downTo lastIndex).withIndex()) {
                        jsonPath("$.content[$listIndex].length()") { value(listMemberSize) }

                        checkMember(
                            this,
                            "$.content[$listIndex]",
                            requests[i],
                            false
                        )

                        nextIndex--
                    }
                }
                .andReturn()
                .response
                .contentAsString
                .toClass(Seek::class)
            nextToken = response.nextToken ?: ""

            page++
        }
    }

    @Test
    @WithMockUser(username = "user-for-create", authorities = ["SCOPE_urn:sample:create:v1"])
    fun `Create should create multiple and return created records`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val request = createRequest(suffix)

        mockMvc
            .post(url) {
                contentType = APPLICATION_JSON
                content = request.toJson()
            }
            .andExpect {
                status { isCreated() }
                header { exists(HEADER_TRACE_ID) }
                content { contentType(APPLICATION_JSON) }

                jsonPath("$") { isNotEmpty() }
                jsonPath("$.length()") { value(detailMemberSize) }

                checkMember(this, "$", request)

                for (translation in request.translations!!) {
                    val translationPath = languagePath("$.translations", translation.language!!)

                    checkTranslation(this, translationPath, translation)
                }
            }
    }

    @Test
    @WithMockUser(
        username = "user-for-create-and-get",
        authorities = ["SCOPE_urn:sample:create:v1", "SCOPE_urn:sample:get:v1"]
    )
    fun `Get should return the created record`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val request = createRequest(suffix)
        val createResponse = mockMvc
            .post(url) {
                contentType = APPLICATION_JSON
                content = request.toJson()
            }
            .andReturn()
            .response
            .contentAsString
            .toClass(SampleResponse::class)
        val uri = "$url/${createResponse.id}"

        mockMvc
            .get(uri) {
                contentType = APPLICATION_JSON
                content = request.toJson()
            }
            .andExpect {
                status { isOk() }
                header { exists(HEADER_TRACE_ID) }
                content { contentType(APPLICATION_JSON) }

                jsonPath("$") { isNotEmpty() }
                jsonPath("$.length()") { value(detailMemberSize) }

                checkMember(this, "$", request)

                for (translation in request.translations!!) {
                    val translationPath = languagePath("$.translations", translation.language!!)

                    checkTranslation(this, translationPath, translation)
                }
            }
    }

    @Test
    @WithMockUser(
        username = "user-for-create-and-update",
        authorities = ["SCOPE_urn:sample:create:v1", "SCOPE_urn:sample:update:v1"]
    )
    fun `Update should return the updated record`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val request = createRequest(suffix)
        val createResponse = mockMvc
            .post(url) {
                contentType = APPLICATION_JSON
                content = request.toJson()
            }
            .andReturn()
            .response
            .contentAsString
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
            }
            .andExpect {
                status { isOk() }
                header { exists(HEADER_TRACE_ID) }
                content { contentType(APPLICATION_JSON) }

                jsonPath("$") { isNotEmpty() }
                jsonPath("$.length()") { value(detailMemberSize) }

                checkMember(this, "$", updatedRequest)

                for (translation in updatedRequest.translations!!) {
                    val translationPath = languagePath("$.translations", translation.language!!)

                    checkTranslation(this, translationPath, translation)
                }
            }
    }

    @Test
    @WithMockUser(
        username = "user-for-create-delete-and-get",
        authorities = ["SCOPE_urn:sample:create:v1", "SCOPE_urn:sample:delete:v1", "SCOPE_urn:sample:get:v1"]
    )
    fun `Delete should return empty then 404 after`() {
        val suffix = RandomStringUtils.randomAlphabetic(8)
        val request = createRequest(suffix)
        val createResponse = mockMvc
            .post(url) {
                contentType = APPLICATION_JSON
                content = request.toJson()
            }
            .andReturn()
            .response
            .contentAsString
            .toClass(SampleResponse::class)
        val uri = "$url/${createResponse.id}"

        mockMvc
            .delete(uri)
            .andExpect {
                status { isNoContent() }
                header { exists(HEADER_TRACE_ID) }

                jsonPath("$") { doesNotExist() }
            }

        sleep(1000)

        val args = arrayOf("data.SampleTable.id", createResponse.id)
        val message = messageSource.getMessage("error.resource.notFound", args, getDefault())

        mockMvc
            .get(uri)
            .andExpect {
                status { isNotFound() }
                header { exists(HEADER_TRACE_ID) }
                content { contentType(APPLICATION_JSON) }

                jsonPath("$") { isArray() }
                jsonPath("$.length()") { value(1) }
                jsonPath("$[0].source") { isArray() }
                jsonPath("$[0].source.length()") { value(3) }
                jsonPath("$[0].source[0]") { value("data") }
                jsonPath("$[0].source[1]") { value("SampleTable") }
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

    private fun checkMember(
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
            jsonPath("$path.createdAt") { isString() }
            jsonPath("$path.createdBy") { isString() }
            jsonPath("$path.lastModifiedAt") { isString() }
            jsonPath("$path.lastModifiedBy") { isString() }

            if (includeTranslations) {
                matchersDsl.jsonPath("$path.translations") { isArray() }
                matchersDsl.jsonPath("$path.translations.length()") { value(request.translations!!.size) }
                matchersDsl.jsonPath("$path.translation") { isMap() }
                matchersDsl.jsonPath("$path.translation.length()") { value(translationMemberSize) }

                request.translations?.forEach { translation ->
                    val translationPath = languagePath("$path.translations", translation.language!!)

                    checkTranslation(matchersDsl, translationPath, translation)
                }
            }
        }
    }

    private fun checkTranslation(
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
