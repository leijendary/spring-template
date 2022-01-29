package com.leijendary.spring.boot.template.core.model

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class IdentityIdModel : AppModel() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long = 0
}