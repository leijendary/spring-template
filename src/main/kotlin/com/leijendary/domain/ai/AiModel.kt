package com.leijendary.domain.ai

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.ai.chat.messages.MessageType
import java.util.UUID

class AiChatRequest {
    var id = UUID.randomUUID()

    @field:NotBlank(message = "validation.required")
    @field:Size(max = 1000, message = "validation.maxLength")
    lateinit var prompt: String
}

data class AiChatResponse(val id: UUID, val text: String)

data class AiChatHistoryResponse(val id: UUID, val messages: List<AiChatMessage>)

data class AiChatMessage(val text: String, val type: MessageType)
