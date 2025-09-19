package com.leijendary.domain.ai.chat

import com.leijendary.model.PrefixedIdEntity
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class AiChat(var title: String = DEFAULT_TITLE) : PrefixedIdEntity() {
    @CreatedDate
    lateinit var createdAt: Instant

    @CreatedBy
    lateinit var createdBy: String

    override fun getIdPrefix(): String {
        return ID_PREFIX
    }

    companion object {
        const val DEFAULT_TITLE = "New Chat"
        const val ENTITY = "chat"
        const val POINTER_ID = "#/data/$ENTITY/id"

        private const val ID_PREFIX = "cht"
    }
}
