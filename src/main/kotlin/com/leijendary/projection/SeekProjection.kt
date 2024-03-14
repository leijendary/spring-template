package com.leijendary.projection

import java.time.Instant

interface SeekProjection {
    val createdAt: Instant
    val id: Long
}
