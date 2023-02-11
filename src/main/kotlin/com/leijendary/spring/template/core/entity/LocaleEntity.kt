package com.leijendary.spring.template.core.entity

import jakarta.persistence.MappedSuperclass

@MappedSuperclass
open class LocaleEntity(open var language: String, open var ordinal: Int) : AppEntity()
