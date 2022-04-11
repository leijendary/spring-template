package com.leijendary.spring.boot.template.api.v1.data

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class SampleRequest(
    @get:NotBlank(message = "validation.required")
    @get:Size(max = 50, message = "validation.maxLength")
    val field1: String? = null,

    val field2: Long = 0,

    @get:DecimalMin(value = "0.00", message = "validation.decimal.min")
    val amount: BigDecimal = ZERO,

    @get:Valid
    @get:NotEmpty(message = "validation.required")
    val translations: MutableSet<SampleTranslationRequest>? = HashSet()
)