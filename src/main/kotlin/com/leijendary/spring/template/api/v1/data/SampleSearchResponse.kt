package com.leijendary.spring.template.api.v1.data

import java.io.Serializable
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*

data class SampleSearchResponse(
    val id: UUID,
    val column1: String,
    val column2: Long,
    val amount: BigDecimal,
    val name: String,
    val description: String,
    val createdAt: OffsetDateTime,
) : Serializable
