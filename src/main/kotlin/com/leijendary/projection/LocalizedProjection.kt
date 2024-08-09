package com.leijendary.projection

interface LocalizedProjection<T : LocaleProjection> {
    var translations: List<T>

    fun getTranslation(language: String): T {
        return translations.firstOrNull { it.language == language } ?: translations.minBy { it.ordinal }
    }
}
