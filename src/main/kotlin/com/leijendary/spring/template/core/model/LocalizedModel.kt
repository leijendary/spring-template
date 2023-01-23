package com.leijendary.spring.template.core.model

import com.leijendary.spring.template.core.util.RequestContext.language
import java.io.Serializable
import java.util.*

abstract class LocalizedModel<T : LocaleModel> : Serializable {
    var id: UUID? = null
    var translations: Set<T> = HashSet()

    val translation: T
        get() {
            val language: String = language
            val sorted = translations.sortedBy { it.ordinal }

            return sorted.firstOrNull { it.language == language } ?: sorted.first()
        }
}
