package com.leijendary.spring.boot.template.core.model

import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class LocaleCopy : AppModel() {
    var language: String? = null
    var ordinal: Int = 0
}