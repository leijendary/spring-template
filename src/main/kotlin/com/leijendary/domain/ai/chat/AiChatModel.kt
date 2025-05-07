package com.leijendary.domain.ai.chat

import com.leijendary.error.CODE_REQUIRED
import com.leijendary.error.CODE_SIZE_RANGE
import com.leijendary.projection.CursorProjection
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.ai.chat.messages.MessageType
import java.time.Instant

data class AiChatRequest(
    val id: String? = null,

    @field:NotBlank(message = CODE_REQUIRED)
    @field:Size(min = 3, max = 1000, message = CODE_SIZE_RANGE)
    val prompt: String = ""
)

data class AiChatResponse(
    override val id: String,
    val title: String,
    override val createdAt: Instant
) : CursorProjection

data class AiChatCreateResponse(val id: String, val text: String)

data class AiChatHistoryResponse(val id: String, val messages: List<AiChatMessage>)

data class AiChatMessage(val text: String, val type: MessageType)
