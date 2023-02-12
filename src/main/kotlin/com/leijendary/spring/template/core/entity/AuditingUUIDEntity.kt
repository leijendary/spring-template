package com.leijendary.spring.template.core.entity

import com.leijendary.spring.template.core.projection.CreatedProjection
import com.leijendary.spring.template.core.projection.LastModifiedProjection
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
open class AuditingUUIDEntity : UUIDEntity(), CreatedProjection, LastModifiedProjection {
    @Version
    var version = 0

    @CreatedDate
    override var createdAt: OffsetDateTime = now

    @CreatedBy
    override var createdBy: String = ""

    @LastModifiedDate
    override var lastModifiedAt: OffsetDateTime = now

    @LastModifiedBy
    override var lastModifiedBy: String = ""
}
