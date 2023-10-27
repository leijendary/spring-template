package com.leijendary.spring.core.projection

import java.time.OffsetDateTime

interface LastModifiedProjection {
    var lastModifiedAt: OffsetDateTime
    var lastModifiedBy: String
}
