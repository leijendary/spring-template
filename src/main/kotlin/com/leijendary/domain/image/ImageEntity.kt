package com.leijendary.domain.image

import com.leijendary.model.ErrorSource
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class Image(var name: String) {
    @Id
    var id: Long = 0

    var mediaType: String? = null
    var validated: Boolean = false

    @CreatedDate
    lateinit var createdAt: Instant

    @CreatedBy
    lateinit var createdBy: String

    companion object {
        const val ENTITY = "image"
        val ERROR_SOURCE_NAME = ErrorSource(pointer = "/data/$ENTITY/name")
        val ERROR_SOURCE_STORAGE_NAME = ErrorSource(pointer = "/storage/$ENTITY/name")
    }
}

@Table
data class ImageMetadata(
    @Id
    var id: Long = 0,

    var name: String,
    var value: String
) : Persistable<Long> {
    override fun getId(): Long {
        return id
    }

    override fun isNew(): Boolean {
        return true
    }
}
