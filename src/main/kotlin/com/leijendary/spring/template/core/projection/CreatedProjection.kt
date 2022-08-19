package com.leijendary.spring.template.core.projection

import java.time.LocalDateTime

interface CreatedProjection {
    var createdAt: LocalDateTime
    var createdBy: String
}