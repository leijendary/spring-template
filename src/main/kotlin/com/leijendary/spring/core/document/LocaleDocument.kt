package com.leijendary.spring.core.document

import com.leijendary.spring.core.projection.LocaleProjection
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType.Integer
import org.springframework.data.elasticsearch.annotations.FieldType.Keyword

open class LocaleDocument(
    @Field(type = Keyword)
    override var language: String,

    @Field(type = Integer)
    override var ordinal: Int = 0
) : LocaleProjection
