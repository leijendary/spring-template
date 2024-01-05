package com.leijendary.model

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

open class TranslationRequest {
    @field:NotBlank(message = "validation.required")
    @field:Size(min = 2, max = 2, message = "validation.size.same")
    var language = ""

    @field:Min(value = 1, message = "validation.min")
    var ordinal = 0
}
