package com.leijendary.projection

import com.leijendary.util.requestContext

interface LocalizedProjection<T : LocaleProjection> {
    val translations: List<T>
    val translation: T
        get() = translations.firstOrNull { it.language == requestContext.language } ?: translations.minBy { it.ordinal }
}
