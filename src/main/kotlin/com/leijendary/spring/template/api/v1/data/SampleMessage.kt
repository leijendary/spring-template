package com.leijendary.spring.template.api.v1.data

import com.leijendary.spring.template.core.data.LocalizedData
import java.math.BigDecimal

data class SampleMessage(
    val column1: String,
    val column2: Long,
    val amount: BigDecimal?
) : LocalizedData<SampleTranslationMessage>()