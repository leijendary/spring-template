package com.leijendary.spring.template.core.model

import com.leijendary.spring.template.core.projection.IdentityProjection
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class IdentityModel : AppModel(), IdentityProjection {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    override var id: Long = 0
}