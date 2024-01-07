package com.leijendary.domain.sample

import com.leijendary.domain.sample.SampleSearch.Companion.INDEX_NAME
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
data class SampleSearch(
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
    override var translations: List<SampleSearchTranslation> = emptyList(),

    @Field(type = Date, format = [date_time])
    var createdAt: OffsetDateTime,

    @CompletionField
    var completion: Completion,
) : LocalizedProjection<SampleSearchTranslation> {
    companion object {
        const val INDEX_NAME = "sample"
    }
}

@JvmRecord
data class SampleSearchTranslation(
    @Field(type = Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    val name: String,

    @Field(type = Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    val description: String?,

    @Field(type = Keyword)
    override val language: String,

    @Field(type = Integer)
    override val ordinal: Int = 0
) : LocaleProjection
