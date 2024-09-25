package com.leijendary.domain.ai

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.ai.chat.messages.MessageType
import java.time.Instant

class AiChatRequest {
    var id: Long = 0

    @field:NotBlank(message = "validation.required")
    @field:Size(max = 1000, message = "validation.maxLength")
    lateinit var prompt: String
}

data class AiChatResponse(val id: Long, val title: String, val createdAt: Instant)

data class AiChatCreateResponse(val id: Long, val text: String)

data class AiChatHistoryResponse(val id: Long, val messages: List<AiChatMessage>)

data class AiChatMessage(val text: String, val type: MessageType)
