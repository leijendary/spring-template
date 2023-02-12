package com.leijendary.spring.template.core.entity

import com.leijendary.spring.template.core.projection.LocaleProjection
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
open class LocaleEntity(override var language: String, override var ordinal: Int) : AppEntity(), LocaleProjection
