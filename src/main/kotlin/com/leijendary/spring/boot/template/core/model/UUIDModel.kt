package com.leijendary.spring.boot.template.core.model

import java.util.*
import java.util.UUID.randomUUID
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class UUIDModel : IdentityModel() {
    var id: UUID = randomUUID()
}