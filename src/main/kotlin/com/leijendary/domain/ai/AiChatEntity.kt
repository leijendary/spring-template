package com.leijendary.domain.ai

import com.leijendary.model.ErrorSource
import com.leijendary.projection.PrefixedIDProjection
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class AiChat(var title: String = DEFAULT_TITLE) : PrefixedIDProjection {
    @Id
    private lateinit var id: String

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
        const val DEFAULT_TITLE = "New Chat"
        const val ENTITY = "chat"
        val ERROR_SOURCE = ErrorSource(pointer = "/data/$ENTITY/id")

        private const val ID_PREFIX = "cht"
    }
}
