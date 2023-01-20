package com.leijendary.spring.template.core.model

import java.time.OffsetDateTime

interface SoftDeleteModel {
    var deletedAt: OffsetDateTime?
    var deletedBy: String?
}
