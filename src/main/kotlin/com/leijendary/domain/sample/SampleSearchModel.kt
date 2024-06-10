package com.leijendary.domain.sample

import com.leijendary.domain.image.ImageResponse
import com.leijendary.domain.sample.SampleSearch.Companion.INDEX_NAME
import com.leijendary.projection.LocaleProjection
import com.leijendary.projection.LocalizedProjection
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.CompletionField
import org.springframework.data.elasticsearch.annotations.DateFormat.date_time
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.FieldType.Date
import org.springframework.data.elasticsearch.annotations.FieldType.Integer
import org.springframework.data.elasticsearch.annotations.FieldType.Keyword
import org.springframework.data.elasticsearch.annotations.FieldType.Nested
import org.springframework.data.elasticsearch.annotations.FieldType.Text
import org.springframework.data.elasticsearch.annotations.InnerField
import org.springframework.data.elasticsearch.annotations.MultiField
import org.springframework.data.elasticsearch.core.suggest.Completion
import java.math.BigDecimal
import java.time.OffsetDateTime

@Document(indexName = INDEX_NAME)
data class SampleSearch(
    @Id
    @Field(type = FieldType.Long)
    var id: Long,

    @MultiField(mainField = Field(type = Text), otherFields = [InnerField(suffix = "keyword", type = Keyword)])
    var name: String,

    @Field(type = Text)
    var description: String?,

    @Field(type = FieldType.Double)
    var amount: BigDecimal,

    @Field(type = Nested, includeInParent = true)
    override var translations: List<SampleSearchTranslation> = emptyList(),

    var image: ImageResponse?,

    @Field(type = Date, format = [date_time])
    var createdAt: OffsetDateTime,

    @CompletionField
    var completion: Completion,
) : LocalizedProjection<SampleSearchTranslation> {
    companion object {
        const val INDEX_NAME = "sample"
    }
}

data class SampleSearchTranslation(
    @MultiField(mainField = Field(type = Text), otherFields = [InnerField(suffix = "keyword", type = Keyword)])
    val name: String,

    @Field(type = Text)
    val description: String?,

    @Field(type = Keyword)
    override val language: String,

    @Field(type = Integer)
    override val ordinal: Int = 0
) : LocaleProjection
