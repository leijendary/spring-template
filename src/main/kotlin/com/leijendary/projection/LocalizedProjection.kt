package com.leijendary.projection

import com.leijendary.util.RequestContext.language

interface LocalizedProjection<T : LocaleProjection> {
    val translations: List<T>
    val translation: T
        get() = translations.firstOrNull { it.language == language } ?: translations.minByOrNull { it.ordinal }!!
}
