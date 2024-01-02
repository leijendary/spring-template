package com.leijendary.domain.sample

import com.leijendary.domain.sample.SampleDocument.Companion.INDEX_NAME
import com.leijendary.projection.LocaleProjection
import com.leijendary.projection.LocalizedProjection
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.*
import org.springframework.data.elasticsearch.annotations.DateFormat.date_time
import org.springframework.data.elasticsearch.annotations.FieldType.*
import org.springframework.data.elasticsearch.core.suggest.Completion
import java.math.BigDecimal
import java.time.OffsetDateTime

@Document(indexName = INDEX_NAME)
@Setting(settingPath = "/elasticsearch/settings.json")
data class SampleDocument(
    @Id
    @Field(type = FieldType.Long)
    var id: Long,

    @Field(type = Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    var name: String,

    @Field(type = Text)
    var description: String?,

    @Field(type = FieldType.Double)
    var amount: BigDecimal,

    @Field(type = Nested, includeInParent = true)
    override var translations: List<SampleTranslationDocument> = emptyList(),

    @Field(type = Date, format = [date_time])
    var createdAt: OffsetDateTime,

    @CompletionField
    var completion: Completion,
) : LocalizedProjection<SampleTranslationDocument> {
    companion object {
        const val INDEX_NAME = "sample"
    }

    fun update(sample: SampleDetail) {
        name = sample.name
        description = sample.description
        amount = sample.amount
        translations = sample.translations.map {
            SampleTranslationDocument(
                name = it.name,
                description = it.description,
                language = it.language,
                ordinal = it.ordinal,
            )
        }
        completion = sample.translations
            .map { it.name }
            .toTypedArray()
            .let(::Completion)
    }
}

data class SampleTranslationDocument(
    @Field(type = Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    var name: String,

    @Field(type = Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    var description: String?,

    @Field(type = Keyword)
    override var language: String,

    @Field(type = Integer)
    override var ordinal: Int = 0
) : LocaleProjection
