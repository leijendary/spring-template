package com.leijendary.projection

import com.leijendary.util.RequestContext.language

interface LocalizedProjection<T : LocaleProjection> {
    var translations: List<T>
    val translation: T
        get() {
            return translations.firstOrNull { it.language == language } ?: translations.minByOrNull { it.ordinal }!!
        }
}
