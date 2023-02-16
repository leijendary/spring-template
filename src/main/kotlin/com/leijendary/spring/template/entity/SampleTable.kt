package com.leijendary.spring.template.entity

import com.leijendary.spring.template.core.entity.AuditingUUIDEntity
import com.leijendary.spring.template.core.entity.LocalizedEntity
import com.leijendary.spring.template.core.entity.SoftDeleteEntity
import jakarta.persistence.*
import jakarta.persistence.FetchType.EAGER
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.OffsetDateTime

@Entity
@Where(clause = "deleted_at is null")
class SampleTable : AuditingUUIDEntity(), LocalizedEntity<SampleTableTranslation>, SoftDeleteEntity {
    @Column(name = "column_1")
    var column1 = ""

    @Column(name = "column_2")
    var column2 = 0L

    var amount: BigDecimal = ZERO

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "sample_table_translation", joinColumns = [JoinColumn(name = "id")])
    override val translations = HashSet<SampleTableTranslation>()

    override var deletedAt: OffsetDateTime? = null
    override var deletedBy: String? = null
}
