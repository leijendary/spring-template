package com.leijendary.spring.template.api.v1.model

import com.leijendary.spring.template.core.model.TranslationRequest
import jakarta.validation.constraints.NotBlank

data class SampleTranslationRequest(
    @get:NotBlank(message = "validation.required")
    val name: String? = null,

    @get:NotBlank(message = "validation.required")
    val description: String? = null
) : TranslationRequest()
