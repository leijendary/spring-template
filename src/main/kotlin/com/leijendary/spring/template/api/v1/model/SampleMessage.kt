package com.leijendary.spring.template.api.v1.model

import com.leijendary.spring.template.core.model.LocalizedModel
import java.math.BigDecimal

data class SampleMessage(
    val column1: String,
    val column2: Long,
    val amount: BigDecimal?
) : LocalizedModel<SampleTranslationMessage>()
