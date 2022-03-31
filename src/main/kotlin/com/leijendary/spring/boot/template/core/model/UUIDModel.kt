package com.leijendary.spring.boot.template.core.model

import java.util.*
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class UUIDModel : AppModel() {
    @Id
    @GeneratedValue
    var id: UUID? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var rowId: Long = 0
}