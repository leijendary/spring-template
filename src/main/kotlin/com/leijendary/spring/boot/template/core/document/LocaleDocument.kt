package com.leijendary.spring.boot.template.core.document

import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType.Integer
import org.springframework.data.elasticsearch.annotations.FieldType.Keyword

abstract class LocaleDocument(
    @Field(type = Keyword)
    var language: String? = null,

    @Field(type = Integer)
    var ordinal: Int = 0
)