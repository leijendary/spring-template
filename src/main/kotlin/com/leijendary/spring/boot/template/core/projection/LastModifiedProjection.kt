package com.leijendary.spring.boot.template.core.projection

import java.time.OffsetDateTime

interface LastModifiedProjection {
    var lastModifiedAt: OffsetDateTime
    var lastModifiedBy: String
}