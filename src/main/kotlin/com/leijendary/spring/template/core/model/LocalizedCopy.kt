package com.leijendary.spring.template.core.model

import com.leijendary.spring.template.core.projection.UUIDProjection
import com.leijendary.spring.template.core.util.RequestContext.language
import java.io.Serializable

interface LocalizedCopy<T : LocaleCopy> : UUIDProjection, Serializable {
    val translations: Set<T>

    val translation: T
        get() {
            val language: String = language
            val sorted = translations.sortedBy { it.ordinal }

            return sorted.firstOrNull { it.language == language } ?: sorted.first()
        }
}
