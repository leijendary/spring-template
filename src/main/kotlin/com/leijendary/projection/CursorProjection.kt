package com.leijendary.projection

import java.time.Instant

interface CursorProjection {
    val id: Long
    val createdAt: Instant
}
