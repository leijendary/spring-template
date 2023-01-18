package com.leijendary.spring.template.model

import com.leijendary.spring.template.core.model.LocalizedModel
import com.leijendary.spring.template.core.model.SoftDeleteModel
import com.leijendary.spring.template.core.model.UUIDModel
import com.leijendary.spring.template.core.util.RequestContext.now
import jakarta.persistence.*
import jakarta.persistence.FetchType.EAGER
import org.hibernate.annotations.Where
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Where(clause = "deleted_at is null")
class SampleTable : UUIDModel(), LocalizedModel<SampleTableTranslation>, SoftDeleteModel {
    @Column(name = "column_1")
    var column1: String = ""

    @Column(name = "column_2")
    var column2 = 0L

    var amount: BigDecimal = ZERO

    @Version
    var version = 0

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "sample_table_translation", joinColumns = [JoinColumn(name = "id")])
    override val translations: Set<SampleTableTranslation> = HashSet()

    @CreatedDate
    var createdAt: LocalDateTime = now

    @CreatedBy
    var createdBy: String = ""

    @LastModifiedDate
    var lastModifiedAt: LocalDateTime = now

    @LastModifiedBy
    var lastModifiedBy: String = ""

    override var deletedAt: LocalDateTime? = null
    override var deletedBy: String? = null
}
