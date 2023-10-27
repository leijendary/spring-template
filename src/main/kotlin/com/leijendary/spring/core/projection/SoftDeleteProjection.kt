package com.leijendary.spring.core.projection

import java.time.OffsetDateTime

interface SoftDeleteProjection {
    var deletedAt: OffsetDateTime?
    var deletedBy: String?
}
