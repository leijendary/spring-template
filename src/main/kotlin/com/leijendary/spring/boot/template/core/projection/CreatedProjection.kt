package com.leijendary.spring.boot.template.core.projection

import java.time.OffsetDateTime

interface CreatedProjection {
    var createdAt: OffsetDateTime
    var createdBy: String
}