package com.leijendary.spring.boot.template.api.v1.data

import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*

data class SampleSearchResponse(
    val id: UUID,
    val column1: String,
    val column2: String,
    val name: String,
    val description: String,
    val createdAt: OffsetDateTime,
) : Serializable