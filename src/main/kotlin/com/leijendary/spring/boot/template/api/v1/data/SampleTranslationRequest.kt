package com.leijendary.spring.boot.template.api.v1.data

import com.leijendary.spring.boot.template.core.data.TranslationRequest
import javax.validation.constraints.NotBlank

data class SampleTranslationRequest(
    @get:NotBlank(message = "validation.required")
    val name: String? = null,

    @get:NotBlank(message = "validation.required")
    val description: String? = null
) : TranslationRequest()