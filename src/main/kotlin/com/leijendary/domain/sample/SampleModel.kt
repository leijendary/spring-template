package com.leijendary.domain.sample

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.leijendary.error.*
import com.leijendary.model.CursorProjection
import com.leijendary.model.ImageProjection
import com.leijendary.model.LocaleProjection
import com.leijendary.model.TranslationRequest
import com.leijendary.validator.annotation.UniqueFields
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import org.springframework.aot.hint.annotation.RegisterReflection
import org.springframework.data.annotation.Transient
import java.math.BigDecimal
import java.time.Instant

data class SampleCursor(
    override val id: String,
    val name: String,
    val description: String?,
    val amount: BigDecimal,
    override val createdAt: Instant
) : CursorProjection

data class SampleRequest(
    @field:NotBlank(message = CODE_REQUIRED)
    @field:Size(min = 3, max = 100, message = CODE_SIZE_RANGE)
    val name: String = "",

    @field:NotNull(message = CODE_REQUIRED)
    val description: String? = null,

    @field:NotNull(message = CODE_REQUIRED)
    @field:DecimalMin(value = "0.01", message = CODE_DECIMAL_MIN)
    @field:DecimalMax(value = "9999999999.99", message = CODE_DECIMAL_MAX)
    val amount: BigDecimal = BigDecimal.ZERO,

    @field:Valid
    @field:NotEmpty(message = CODE_REQUIRED)
    @field:UniqueFields(["language", "ordinal"])
    val translations: List<SampleTranslationRequest> = emptyList(),

    @field:NotNull(message = CODE_REQUIRED)
    @field:Min(value = 1, message = CODE_MIN)
    val version: Int = 1
)

data class SampleTranslationRequest(
    @field:NotBlank(message = CODE_REQUIRED)
    @field:Size(min = 1, max = 100, message = CODE_SIZE_RANGE)
    val name: String = "",

    @field:Size(min = 3, max = 200, message = CODE_SIZE_RANGE)
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
    var image: ImageProjection? = null
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
    var image: ImageProjection? = null
}

data class SampleTranslationResponse(
    var name: String,
    var description: String?,
    override var language: String,
    override var ordinal: Int
) : LocaleProjection

@RegisterReflection
data class SampleMessage(
    val id: String,
    var name: String,
    var description: String?,
    val amount: BigDecimal,
    val version: Int,
    val translations: List<SampleTranslationMessage>,
    val image: ImageProjection?,
    val createdAt: Instant,
    val lastModifiedAt: Instant
)

@RegisterReflection
data class SampleTranslationMessage(
    var name: String,
    var description: String?,
    override var language: String,
    override var ordinal: Int,
) : LocaleProjection
