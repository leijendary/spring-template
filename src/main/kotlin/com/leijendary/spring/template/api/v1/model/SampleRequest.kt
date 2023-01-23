package com.leijendary.spring.template.api.v1.model

import com.leijendary.spring.template.core.validator.annotation.UniqueFields
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.math.BigDecimal

data class SampleRequest(
    @field:NotBlank(message = "validation.required")
    @field:Size(max = 100, message = "validation.maxLength")
    val field1: String? = null,

    @field:NotNull(message = "validation.required")
    @field:Min(value = 0, message = "validation.min")
    val field2: Long? = null,

    @field:NotNull(message = "validation.required")
    @field:DecimalMin(value = "0.01", message = "validation.decimal.min")
    @field:DecimalMax(value = "9999999999.99", message = "validation.decimal.max")
    val amount: BigDecimal?,

    @field:Valid
    @field:UniqueFields(uniqueFields = ["name", "language", "ordinal"])
    @field:NotEmpty(message = "validation.required")
    val translations: List<SampleTranslationRequest>? = ArrayList()
)
