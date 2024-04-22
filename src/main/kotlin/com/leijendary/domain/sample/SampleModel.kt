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
    val name: String = ""

    @field:NotNull(message = "validation.required")
    val description: String? = null

    @field:NotNull(message = "validation.required")
    @field:DecimalMin(value = "0.01", message = "validation.decimal.min")
    @field:DecimalMax(value = "9999999999.99", message = "validation.decimal.max")
    val amount: BigDecimal = ZERO

    @field:Valid
    @field:UniqueFields(["name", "language", "ordinal"])
    @field:NotEmpty(message = "validation.required")
    val translations = arrayListOf<SampleTranslationRequest>()
}

class SampleTranslationRequest : TranslationRequest() {
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 100, message = "validation.maxLength")
    var name = ""

    @field:Size(max = 200, message = "validation.maxLength")
    val description: String? = null
}

data class SampleList(
    override val id: Long,
    val name: String,
    val description: String?,
    val amount: BigDecimal,
    override val createdAt: OffsetDateTime
) : SeekProjection

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

data class SampleTranslation(
    val name: String,
    val description: String?,
    override val language: String,
    override val ordinal: Int
) : LocaleProjection

data class SampleTranslationsBinds(private val size: Int) {
    val names = arrayOfNulls<String>(size)
    val descriptions = arrayOfNulls<String?>(size)
    val languages = arrayOfNulls<String>(size)
    val ordinals = arrayOfNulls<Int>(size)
}

fun List<SampleTranslationRequest>.toBinds(): SampleTranslationsBinds {
    val binds = SampleTranslationsBinds(size)

    forEachIndexed { i, v ->
        binds.names[i] = v.name
        binds.descriptions[i] = v.description
        binds.languages[i] = v.language
        binds.ordinals[i] = v.ordinal
    }

    return binds
}
