package com.leijendary.spring.boot.template.core.document

import com.leijendary.spring.boot.template.core.util.RequestContext.language
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType.Nested

abstract class LocalizedDocument<T : LocaleDocument> {
    @Field(type = Nested, includeInParent = true)
    var translations: Set<T> = HashSet()

    val translation: T
        get() {
            val language: String = language
            val sorted = translations.sortedBy { it!!.ordinal }

            return sorted.first { it!!.language == language } ?: sorted.first()
        }
}