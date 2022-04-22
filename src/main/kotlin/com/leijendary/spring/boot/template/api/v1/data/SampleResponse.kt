package com.leijendary.spring.boot.template.api.v1.data

import com.leijendary.spring.boot.template.core.data.LocalizedData
import java.math.BigDecimal
import java.time.OffsetDateTime

data class SampleResponse(
    val column1: String,
    val column2: Int,
    val amount: BigDecimal,
    val createdAt: OffsetDateTime,
    val createdBy: String,
    val lastModifiedAt: OffsetDateTime,
    val lastModifiedBy: String,
) : LocalizedData<SampleTranslationResponse>()