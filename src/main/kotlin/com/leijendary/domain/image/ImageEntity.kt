package com.leijendary.domain.image

import com.leijendary.model.ErrorSource
import com.leijendary.projection.PrefixedIDProjection
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class Image(var name: String) : PrefixedIDProjection {
    @Id
    private lateinit var id: String

    var mediaType: String? = null
    var validated: Boolean = false

    @CreatedDate
    lateinit var createdAt: Instant

    @CreatedBy
    lateinit var createdBy: String

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
        const val ENTITY = "image"
        val ERROR_SOURCE_NAME = ErrorSource(pointer = "/data/$ENTITY/name")
        val ERROR_SOURCE_STORAGE_NAME = ErrorSource(pointer = "/storage/$ENTITY/name")

        private const val ID_PREFIX = "img"
    }
}

@Table
data class ImageMetadata(var name: String, var value: String) : Persistable<String> {
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
