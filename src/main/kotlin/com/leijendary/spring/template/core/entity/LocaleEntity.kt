package com.leijendary.spring.template.core.entity

import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class LocaleEntity(open var language: String, open var ordinal: Int) : AppEntity()
