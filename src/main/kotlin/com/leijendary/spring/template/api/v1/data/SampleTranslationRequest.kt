package com.leijendary.spring.template.api.v1.data

import com.leijendary.spring.template.core.data.TranslationRequest
import jakarta.validation.constraints.NotBlank

data class SampleTranslationRequest(
    @get:NotBlank(message = "validation.required")
    val name: String? = null,

    @get:NotBlank(message = "validation.required")
    val description: String? = null
) : TranslationRequest()