package com.leijendary.domain.sample

import com.leijendary.model.PrefixedIDEntity
import com.leijendary.projection.ImageProjection
import com.leijendary.projection.LocaleProjection
import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.Instant

@Table
data class Sample(var name: String, var description: String?, var amount: BigDecimal) : PrefixedIDEntity() {
    @Version
    var version: Int = 0

    @CreatedDate
    lateinit var createdAt: Instant

    @CreatedBy
    lateinit var createdBy: String

    @LastModifiedDate
    lateinit var lastModifiedAt: Instant

    @LastModifiedBy
    lateinit var lastModifiedBy: String

    override fun getIdPrefix(): String {
        return ID_PREFIX
    }

    companion object {
        const val ENTITY = "sample"
        const val POINTER_ID = "#/data/$ENTITY/id"

        private const val ID_PREFIX = "spl"
    }
}

@Table
data class SampleTranslation(
    var name: String,
    var description: String?,
    override var language: String,
    override var ordinal: Int
) : LocaleProjection, Persistable<String> {
    @Id
    private lateinit var id: String

    override fun getId(): String {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    override fun isNew(): Boolean {
        return true
    }
}

@Table
data class SampleImage(
    override var original: String,
    override var preview: String,
    override var thumbnail: String
) : ImageProjection, Persistable<String> {
    @Id
    private lateinit var id: String

    override fun getId(): String {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    override fun isNew(): Boolean {
        return true
    }
}

fun Sample.toDetailResponse(translations: List<SampleTranslation>): SampleDetailResponse {
    return SampleDetailResponse(id, name, description, amount, version, createdAt).apply {
        this.translations.addAll(translations.toResponses())
    }
}

fun Sample.updateWith(request: SampleRequest) {
    name = request.name
    description = request.description
    amount = request.amount
    version = request.version
}

fun List<SampleTranslation>.toResponses() = map { it.toResponse() }

fun SampleTranslation.toResponse() = SampleTranslationResponse(name, description, language, ordinal)
