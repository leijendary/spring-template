package com.leijendary.domain.sample

import com.leijendary.model.ErrorSource
import com.leijendary.projection.ImageProjection
import com.leijendary.projection.LocaleProjection
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
data class Sample(var name: String, var description: String?, var amount: BigDecimal) {
    @Id
    var id: Long = 0

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

    companion object {
        const val ENTITY = "sample"
        val ERROR_SOURCE = ErrorSource(pointer = "/data/$ENTITY/id")
    }
}

@Table
data class SampleTranslation(
    @Id
    var id: Long = 0,

    var name: String,
    var description: String?,
    override var language: String,
    override var ordinal: Int
) : LocaleProjection, Persistable<Long> {
    override fun getId(): Long {
        return id
    }

    override fun isNew(): Boolean {
        return true
    }
}

@Table
data class SampleImage(
    @Id
    var id: Long = 0,

    override var original: String,
    override var preview: String,
    override var thumbnail: String
) : ImageProjection, Persistable<Long> {
    override fun getId(): Long {
        return id
    }

    override fun isNew(): Boolean {
        return true
    }
}
