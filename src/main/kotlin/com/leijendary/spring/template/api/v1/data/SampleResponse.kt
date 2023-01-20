package com.leijendary.spring.template.api.v1.data

import com.leijendary.spring.template.core.data.LocalizedData
import java.math.BigDecimal
import java.time.OffsetDateTime

data class SampleResponse(
    val column1: String,
    val column2: Long,
    val amount: BigDecimal,
    val createdAt: OffsetDateTime,
    val createdBy: String,
    val lastModifiedAt: OffsetDateTime,
    val lastModifiedBy: String,
) : LocalizedData<SampleTranslationResponse>()
