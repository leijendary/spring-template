package com.leijendary.spring.boot.template.document

import com.leijendary.spring.boot.template.core.document.LocaleDocument
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType.Text

class SampleTranslationDocument(
    @Field(type = Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    var name: String,

    @Field(type = Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    var description: String
) : LocaleDocument()