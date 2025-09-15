package com.leijendary.domain.sample

import com.leijendary.model.ImageProjection
import com.leijendary.model.LocaleProjection
import com.leijendary.model.PrefixedIdEntity
import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.Instant

@Table
data class Sample(var name: String, var description: String?, var amount: BigDecimal) : PrefixedIdEntity() {
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

    @Transient
    var translations: MutableList<SampleTranslation> = mutableListOf()

    @Transient
    var image: SampleImage? = null

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
