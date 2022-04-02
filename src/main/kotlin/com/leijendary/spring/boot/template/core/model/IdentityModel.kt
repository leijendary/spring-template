package com.leijendary.spring.boot.template.core.model

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class IdentityModel : AppModel() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var pk: Long = 0
}