package com.leijendary.spring.boot.template.core.data

import com.leijendary.spring.boot.template.core.util.RequestContext.language
import java.io.Serializable
import java.util.*

abstract class LocalizedData<T : LocaleData?> : Serializable {
    var id: UUID? = null
    var translations: Set<T> = HashSet()

    val translation: T
        get() {
            val language: String = language
            val sorted = translations.sortedBy { it!!.ordinal }

            return sorted.first { it!!.language == language } ?: sorted.first()
        }
}