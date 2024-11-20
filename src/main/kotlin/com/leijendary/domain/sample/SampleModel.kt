package com.leijendary.domain.sample

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.leijendary.domain.image.ImageResponse
import com.leijendary.model.TranslationRequest
import com.leijendary.projection.CursorProjection
import com.leijendary.projection.LocaleProjection
import com.leijendary.validator.annotation.UniqueFields
import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.Transient
import java.math.BigDecimal
import java.time.Instant

data class SampleRequest(
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 100, message = "validation.maxLength")
    val name: String = "",

    @field:NotNull(message = "validation.required")
    val description: String? = null,

    @field:NotNull(message = "validation.required")
    @field:DecimalMin(value = "0.01", message = "validation.decimal.min")
    @field:DecimalMax(value = "9999999999.99", message = "validation.decimal.max")
    val amount: BigDecimal = BigDecimal.ZERO,

    @field:Valid
    @field:NotEmpty(message = "validation.required")
    @field:UniqueFields(["language", "ordinal"])
    val translations: List<SampleTranslationRequest> = emptyList(),

    @field:NotNull(message = "validation.required")
    @field:Min(value = 1, message = "validation.min")
    val version: Int = 1
)

data class SampleTranslationRequest(
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 100, message = "validation.maxLength")
    val name: String = "",

    @field:Size(max = 200, message = "validation.maxLength")
    val description: String? = null
) : TranslationRequest()

data class SampleResponse(
    override val id: String,
    val name: String,
    val description: String?,
    val amount: BigDecimal,
    override val createdAt: Instant
) : CursorProjection {
    @Transient
    var image: ImageResponse? = null
}

data class SampleDetailResponse(
    val id: String,
    var name: String,
    var description: String?,
    val amount: BigDecimal,
    val version: Int,
    val createdAt: Instant,
) {
    @Transient
    @JsonInclude(NON_EMPTY)
    var translations: MutableList<SampleTranslationResponse> = mutableListOf()

    @Transient
    var image: ImageResponse? = null
}

data class SampleTranslationResponse(
    var name: String,
    var description: String?,
    override var language: String,
    override var ordinal: Int
) : LocaleProjection

fun SampleRequest.toEntity() = Sample(name, description, amount)

fun List<SampleTranslationRequest>.toEntities(id: String) = map { it.toEntity(id) }

fun SampleTranslationRequest.toEntity(id: String): SampleTranslation {
    return SampleTranslation(name, description, language, ordinal).apply { this.id = id }
}

fun SampleDetailResponse.applyTranslation(translation: SampleTranslation) {
    name = translation.name
    description = translation.description ?: description
}
