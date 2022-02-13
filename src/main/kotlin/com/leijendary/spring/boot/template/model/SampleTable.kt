package com.leijendary.spring.boot.template.model

import com.leijendary.spring.boot.template.core.model.LocalizedModel
import com.leijendary.spring.boot.template.core.model.SoftDeleteModel
import com.leijendary.spring.boot.template.core.util.RequestContext.now
import org.hibernate.annotations.Where
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Version

@Entity
@EntityListeners(AuditingEntityListener::class)
@Where(clause = "deleted_at is null")
class SampleTable : LocalizedModel<SampleTableTranslations>(), SoftDeleteModel {
    @Column(name = "column_1")
    var column1: String = ""

    @Column(name = "column_2")
    var column2 = 0

    var amount: BigDecimal = ZERO

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

    override var deletedAt: OffsetDateTime? = null
    override var deletedBy: String? = null
}