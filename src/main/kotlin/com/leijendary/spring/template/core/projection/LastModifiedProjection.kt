package com.leijendary.spring.template.core.projection

import java.time.LocalDateTime

interface LastModifiedProjection {
    var lastModifiedAt: LocalDateTime
    var lastModifiedBy: String
}