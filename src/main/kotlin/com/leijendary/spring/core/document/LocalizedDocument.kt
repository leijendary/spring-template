package com.leijendary.spring.core.document

import com.leijendary.spring.core.projection.LocalizedProjection
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType.Nested

open class LocalizedDocument<T : LocaleDocument>(
    override var id: Long,

    @Field(type = Nested, includeInParent = true)
    override var translations: MutableSet<T> = mutableSetOf()
) : LocalizedProjection<T>
