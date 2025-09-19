package com.leijendary.domain.ai.chat

import io.mcarle.konvert.api.Konverter

@Konverter
interface AiChatMapper {
    fun toResponse(cursor: AiChatCursor): AiChatResponse
}
