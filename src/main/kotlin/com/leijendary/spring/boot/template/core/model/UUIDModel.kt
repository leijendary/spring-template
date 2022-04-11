package com.leijendary.spring.boot.template.core.model

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
    var id: UUID = randomUUID()

    @Column(insertable = false)
    var rowId: Long? = null
}