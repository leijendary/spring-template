package com.leijendary.projection

import java.time.OffsetDateTime

interface SeekProjection {
    val createdAt: OffsetDateTime
    val id: Long
}
