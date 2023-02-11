package com.leijendary.spring.template.core.entity

import com.leijendary.spring.template.core.projection.IdentityProjection
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
open class IdentityEntity : AppEntity(), IdentityProjection {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    override var id: Long = 0
}
