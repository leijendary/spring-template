package com.leijendary.spring.template.core.data

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

open class TranslationRequest(
    @field:NotBlank(message = "validation.required")
    @field:Size(min = 2, max = 2, message = "validation.size.same")
    val language: String? = null,

    @get:Min(value = 1, message = "validation.min")
    val ordinal: Int = 0
)