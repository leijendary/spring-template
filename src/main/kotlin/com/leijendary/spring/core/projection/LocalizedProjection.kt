package com.leijendary.spring.core.projection

import com.leijendary.spring.core.util.RequestContext.language

interface LocalizedProjection<T : LocaleProjection> : IdentityProjection {
    val translations: MutableSet<T>
    val translation: T
        get() {
            val language = language
            val sorted = translations.sortedBy { it.ordinal }

            return sorted.firstOrNull { it.language == language } ?: sorted.first()
        }
}
