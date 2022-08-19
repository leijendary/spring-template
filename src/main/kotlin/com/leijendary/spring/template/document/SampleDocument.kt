package com.leijendary.spring.template.document

import com.leijendary.spring.template.core.document.LocalizedDocument
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.DateFormat.date_hour_minute_second_millis
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.FieldType.*
import org.springframework.data.elasticsearch.annotations.FieldType.Date
import org.springframework.data.elasticsearch.annotations.FieldType.Double
import org.springframework.data.elasticsearch.annotations.Setting
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Document(indexName = "sample")
@Setting(settingPath = "/elasticsearch/ngram-analyzer.settings.json")
data class SampleDocument(
    @Id
    @Field(type = Keyword)
    var id: UUID,

    @Field(type = Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    var column1: String,

    @Field(type = FieldType.Long)
    var column2: Long,

    @Field(type = Double)
    var amount: BigDecimal,

    @Field(type = Date, format = [date_hour_minute_second_millis])
    var createdAt: LocalDateTime
) : LocalizedDocument<SampleTranslationDocument>()