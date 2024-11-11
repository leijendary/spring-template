package com.leijendary.domain.ai

import com.leijendary.projection.CursorProjection
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.ai.chat.messages.MessageType
import java.time.Instant

class AiChatRequest {
    var id: String? = null

    @field:NotBlank(message = "validation.required")
    @field:Size(max = 1000, message = "validation.maxLength")
    lateinit var prompt: String
}

data class AiChatResponse(
    override val id: String,
    val title: String,
    override val createdAt: Instant
) : CursorProjection

data class AiChatCreateResponse(val id: String, val text: String)

data class AiChatHistoryResponse(val id: String, val messages: List<AiChatMessage>)

data class AiChatMessage(val text: String, val type: MessageType)
