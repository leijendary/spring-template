package com.leijendary.spring.boot.template.document

import com.leijendary.spring.boot.template.core.document.LocalizedDocument
import org.springframework.data.elasticsearch.annotations.DateFormat.date_time
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType.*
import org.springframework.data.elasticsearch.annotations.FieldType.Date
import org.springframework.data.elasticsearch.annotations.Setting
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.Id

@Document(indexName = "sample")
@Setting(settingPath = "/elasticsearch/ngram-analyzer.settings.json")
data class SampleDocument(
    @Id
    @Field(type = Keyword)
    var id: UUID,

    @Field(type = Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    var column1: String,

    @Field(type = Integer)
    var column2: Int,

    @Field(type = Text)
    var amount: BigDecimal,

    @Field(type = Date, format = [date_time])
    var createdAt: OffsetDateTime
) : LocalizedDocument<SampleTranslationDocument>()