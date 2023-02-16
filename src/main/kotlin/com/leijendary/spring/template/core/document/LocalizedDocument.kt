package com.leijendary.spring.template.core.document

import com.leijendary.spring.template.core.util.RequestContext.language
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType.Nested

open class LocalizedDocument<T : LocaleDocument> {
    @Field(type = Nested, includeInParent = true)
    var translations = HashSet<T>()

    val translation: T
        get() {
            val language = language
            val sorted = translations.sortedBy { it.ordinal }

            return sorted.firstOrNull { it.language == language } ?: sorted.first()
        }
}
