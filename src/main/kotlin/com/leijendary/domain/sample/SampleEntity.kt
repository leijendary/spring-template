package com.leijendary.domain.sample

import com.leijendary.model.ErrorSource
import com.leijendary.projection.ImageProjection
import com.leijendary.projection.LocaleProjection
import com.leijendary.projection.PrefixedIDProjection
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.Instant

@Table
data class Sample(var name: String, var description: String?, var amount: BigDecimal) : PrefixedIDProjection {
    @Id
    private lateinit var id: String

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

    override fun getPrefix(): String {
        return ID_PREFIX
    }

    override fun setId(id: String) {
        this.id = id
    }

    override fun getId(): String {
        return id
    }

    override fun isNew(): Boolean {
        return !this::id.isInitialized
    }

    companion object {
        const val ENTITY = "sample"
        val ERROR_SOURCE = ErrorSource(pointer = "/data/$ENTITY/id")

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
