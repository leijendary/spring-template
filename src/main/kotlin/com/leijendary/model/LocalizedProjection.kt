package com.leijendary.model

interface LocalizedProjection<T : LocaleProjection> {
    var translations: List<T>

    fun getTranslationOrFirst(language: String): T {
        return translations.firstOrNull { it.language == language } ?: translations.minBy { it.ordinal }
    }
}
