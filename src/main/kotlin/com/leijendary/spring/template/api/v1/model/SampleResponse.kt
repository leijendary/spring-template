package com.leijendary.spring.template.api.v1.model

import com.leijendary.spring.template.core.model.LocalizedModel
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
) : LocalizedModel<SampleTranslationResponse>()
