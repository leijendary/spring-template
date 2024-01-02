package com.leijendary.domain.sample

import com.leijendary.model.TranslationRequest
import com.leijendary.projection.LocaleProjection
import com.leijendary.projection.SeekProjection
import com.leijendary.validator.annotation.UniqueFields
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.OffsetDateTime

@JvmRecord
data class SampleRequest(
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 100, message = "validation.maxLength")
    val name: String? = null,

    @field:NotNull(message = "validation.required")
    val description: String? = null,

    @field:NotNull(message = "validation.required")
    @field:DecimalMin(value = "0.01", message = "validation.decimal.min")
    @field:DecimalMax(value = "9999999999.99", message = "validation.decimal.max")
    val amount: BigDecimal?,

    @field:Valid
    @field:UniqueFields(uniqueFields = ["name", "language", "ordinal"])
    @field:NotEmpty(message = "validation.required")
    val translations: List<SampleTranslationRequest>? = arrayListOf()
)

data class SampleTranslationRequest(
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 100, message = "validation.maxLength")
    val name: String? = null,

    @field:Size(max = 200, message = "validation.maxLength")
    val description: String? = null
) : TranslationRequest()

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
    val translations: MutableList<SampleTranslation> = mutableListOf(),
    val createdAt: OffsetDateTime
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
