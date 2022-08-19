package com.leijendary.spring.template.core.model

import java.time.LocalDateTime

interface SoftDeleteModel {
    var deletedAt: LocalDateTime?
    var deletedBy: String?
}