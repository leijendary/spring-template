package com.leijendary.spring.template.core.model

import com.leijendary.spring.template.core.util.RequestContext.now
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class AuditingUUIDModel : UUIDModel() {
    @Version
    var version = 0

    @CreatedDate
    var createdAt: LocalDateTime = now

    @CreatedBy
    var createdBy: String = ""

    @LastModifiedDate
    var lastModifiedAt: LocalDateTime = now

    @LastModifiedBy
    var lastModifiedBy: String = ""
}
