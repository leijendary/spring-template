package com.leijendary.spring.template.core.model

import java.util.*
import java.util.UUID.randomUUID
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class UUIDModel {
    @Id
    @GeneratedValue
    @Column(updatable = false)
    var id: UUID = randomUUID()

    @Column(insertable = false, updatable = false)
    var rowId: Long? = null
}