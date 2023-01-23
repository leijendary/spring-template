package com.leijendary.spring.template.core.entity

import com.leijendary.spring.template.core.util.RequestContext.now
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import java.util.*

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class AuditingUUIDEntity : UUIDEntity() {
    @Version
    var version = 0

    @CreatedDate
    var createdAt: OffsetDateTime = now

    @CreatedBy
    var createdBy: String = ""

    @LastModifiedDate
    var lastModifiedAt: OffsetDateTime = now

    @LastModifiedBy
    var lastModifiedBy: String = ""
}
