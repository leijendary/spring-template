package com.leijendary.spring.template.core.entity

import java.time.OffsetDateTime

interface SoftDeleteEntity {
    var deletedAt: OffsetDateTime?
    var deletedBy: String?
}
