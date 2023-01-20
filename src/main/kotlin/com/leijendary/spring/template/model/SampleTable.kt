package com.leijendary.spring.template.model

import com.leijendary.spring.template.core.model.AuditingUUIDModel
import com.leijendary.spring.template.core.model.LocalizedModel
import com.leijendary.spring.template.core.model.SoftDeleteModel
import jakarta.persistence.*
import jakarta.persistence.FetchType.EAGER
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.LocalDateTime

@Entity
@Where(clause = "deleted_at is null")
class SampleTable : AuditingUUIDModel(), LocalizedModel<SampleTableTranslation>, SoftDeleteModel {
    @Column(name = "column_1")
    var column1: String = ""

    @Column(name = "column_2")
    var column2 = 0L

    var amount: BigDecimal = ZERO

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "sample_table_translation", joinColumns = [JoinColumn(name = "id")])
    override val translations: Set<SampleTableTranslation> = HashSet()

    override var deletedAt: LocalDateTime? = null
    override var deletedBy: String? = null
}
