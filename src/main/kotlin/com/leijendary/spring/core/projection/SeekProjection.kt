package com.leijendary.spring.core.projection

import java.time.OffsetDateTime

interface SeekProjection {
    var id: Long
    var createdAt: OffsetDateTime
}
