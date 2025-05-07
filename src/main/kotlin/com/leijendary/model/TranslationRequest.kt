package com.leijendary.model

import com.leijendary.error.CODE_MIN
import com.leijendary.error.CODE_REQUIRED
import com.leijendary.error.CODE_SIZE_SAME
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

open class TranslationRequest {
    @field:NotBlank(message = CODE_REQUIRED)
    @field:Size(min = 2, max = 2, message = CODE_SIZE_SAME)
    var language = ""

    @field:Min(value = 1, message = CODE_MIN)
    var ordinal = 0
}
