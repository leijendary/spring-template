package com.leijendary.spring.template.core.model

import java.util.*
import java.util.UUID.randomUUID
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class UUIDModel {
    @Id
    @GeneratedValue
    @Column(updatable = false)
    var id: UUID = randomUUID()

    @Column(insertable = false, updatable = false)
    var rowId: Long? = null
}