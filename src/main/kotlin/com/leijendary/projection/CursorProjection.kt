package com.leijendary.projection

import java.time.Instant

interface CursorProjection {
    val id: String
    val createdAt: Instant
}
