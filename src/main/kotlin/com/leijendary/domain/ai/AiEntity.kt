package com.leijendary.domain.ai

import org.springframework.ai.chat.messages.MessageType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class AiChatMemory(
    @Id
    val sessionId: String,

    val content: String,
    val type: MessageType
) : Persistable<String> {
    @CreatedDate
    lateinit var timestamp: Instant

    override fun getId(): String {
        return sessionId
    }

    override fun isNew(): Boolean {
        return true
    }
}
