package com.leijendary.spring.template.core.entity

import com.leijendary.spring.template.core.projection.UUIDProjection
import com.leijendary.spring.template.core.util.RequestContext.language

interface LocalizedEntity<T : LocaleEntity> : UUIDProjection {
    val translations: Set<T>

    val translation: T
        get() {
            val language = language
            val sorted = translations.sortedBy { it.ordinal }

            return sorted.firstOrNull { it.language == language } ?: sorted.first()
        }
}
