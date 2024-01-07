package com.leijendary.domain.sample

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.leijendary.model.TranslationRequest
import com.leijendary.projection.LocaleProjection
import com.leijendary.projection.SeekProjection
import com.leijendary.validator.annotation.UniqueFields
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.OffsetDateTime

class SampleRequest {
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 100, message = "validation.maxLength")
    var name = ""

    @field:NotNull(message = "validation.required")
    var description: String? = null

    @field:NotNull(message = "validation.required")
    @field:DecimalMin(value = "0.01", message = "validation.decimal.min")
    @field:DecimalMax(value = "9999999999.99", message = "validation.decimal.max")
    var amount: BigDecimal = ZERO

    @field:Valid
    @field:UniqueFields(uniqueFields = ["name", "language", "ordinal"])
    @field:NotEmpty(message = "validation.required")
    var translations = arrayListOf<SampleTranslationRequest>()
}

class SampleTranslationRequest : TranslationRequest() {
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 100, message = "validation.maxLength")
    var name = ""

    @field:Size(max = 200, message = "validation.maxLength")
    val description: String? = null
}

@JvmRecord
data class SampleList(
    override val id: Long,
    val name: String,
    val description: String?,
    val amount: BigDecimal,
    override val createdAt: OffsetDateTime
) : SeekProjection

@JvmRecord
data class SampleDetail(
    val id: Long,
    val name: String,
    val description: String?,
    val amount: BigDecimal,
    val version: Int,
    val createdAt: OffsetDateTime,

    @JsonInclude(NON_EMPTY)
    val translations: MutableList<SampleTranslation> = mutableListOf(),
)

@JvmRecord
data class SampleTranslation(
    val name: String,
    val description: String?,
    override val language: String,
    override val ordinal: Int
) : LocaleProjection

@JvmRecord
data class SampleTranslationsBinds(
    val ids: List<Long>,
    val names: MutableList<String>,
    val descriptions: MutableList<String?>,
    val languages: MutableList<String>,
    val ordinals: MutableList<Int>
)
