package com.leijendary.domain.sample

import com.leijendary.domain.sample.SampleSearch.Companion.INDEX_NAME
import com.leijendary.model.ImageProjection
import com.leijendary.model.LocaleProjection
import com.leijendary.model.LocalizedProjection
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.*
import org.springframework.data.elasticsearch.annotations.DateFormat.date_time
import org.springframework.data.elasticsearch.annotations.FieldType.*
import org.springframework.data.elasticsearch.core.suggest.Completion
import java.math.BigDecimal
import java.time.Instant

@Document(indexName = INDEX_NAME)
data class SampleSearch(
    @Id
    @Field(type = Keyword)
    var id: String,

    @MultiField(mainField = Field(type = Text), otherFields = [InnerField(suffix = "keyword", type = Keyword)])
    var name: String,

    @Field(type = Text)
    var description: String?,

    @Field(type = FieldType.Double)
    var amount: BigDecimal,

    @Field(type = Nested, includeInParent = true)
    override var translations: List<SampleTranslationSearch> = emptyList(),

    var image: ImageProjection?,

    @Field(type = Date, format = [date_time])
    var createdAt: Instant,

    @CompletionField
    var completion: Completion,
) : LocalizedProjection<SampleTranslationSearch> {
    companion object {
        const val INDEX_NAME = "sample"
        const val POINTER_ID = "#/search/$INDEX_NAME/id"
    }
}

data class SampleTranslationSearch(
    @MultiField(mainField = Field(type = Text), otherFields = [InnerField(suffix = "keyword", type = Keyword)])
    val name: String,

    @Field(type = Text)
    val description: String?,

    @Field(type = Keyword)
    override val language: String,

    @Field(type = Integer)
    override val ordinal: Int
) : LocaleProjection
