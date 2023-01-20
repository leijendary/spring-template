package com.leijendary.spring.template.core.model

import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class LocaleModel(open var language: String, open var ordinal: Int) : AppModel()