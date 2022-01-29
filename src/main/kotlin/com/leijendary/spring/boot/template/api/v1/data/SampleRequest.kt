package com.leijendary.spring.boot.template.api.v1.data

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class SampleRequest(
    @NotBlank(message = "validation.required")
    @Size(max = 50, message = "validation.maxLength")
    val field1: String? = null,

    val field2: Int = 0,

    @Valid
    @NotEmpty(message = "validation.required")
    val translations: MutableSet<SampleTranslationRequest>? = HashSet()
)