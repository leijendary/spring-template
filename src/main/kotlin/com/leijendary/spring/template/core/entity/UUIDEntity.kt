package com.leijendary.spring.template.core.entity

import com.leijendary.spring.template.core.projection.UUIDProjection
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.util.*
import java.util.UUID.randomUUID

@MappedSuperclass
open class UUIDEntity : UUIDProjection {
    @Id
    @GeneratedValue
    @Column(updatable = false)
    override var id: UUID = randomUUID()

    @Column(insertable = false, updatable = false)
    var rowId: Long? = null
}
