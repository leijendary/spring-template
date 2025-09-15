package com.leijendary.model

import java.time.Instant

interface CursorProjection {
    val id: String
    val createdAt: Instant
}
