package com.leijendary.spring.boot.template.core.data

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

open class TranslationRequest(
    @NotBlank(message = "validation.required")
    val language: String? = null,

    @Min(value = 1, message = "validation.min")
    val ordinal: Int = 0
)