package com.leijendary.spring.core.projection

import java.time.OffsetDateTime

interface CreatedProjection {
    var createdAt: OffsetDateTime
    var createdBy: String
}
