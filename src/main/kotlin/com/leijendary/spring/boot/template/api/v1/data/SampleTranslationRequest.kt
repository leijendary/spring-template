package com.leijendary.spring.boot.template.api.v1.data

import com.leijendary.spring.boot.template.core.data.TranslationRequest
import javax.validation.constraints.NotBlank

data class SampleTranslationRequest(
    @NotBlank(message = "validation.required")
    val name: String? = null,

    @NotBlank(message = "validation.required")
    val description: String? = null
) : TranslationRequest()