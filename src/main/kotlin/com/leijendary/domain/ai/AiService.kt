package com.leijendary.domain.ai

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_RESPONSE_SIZE
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.UUID

interface AiService {
    fun chat(request: AiChatRequest): Flux<AiChatResponse>
    fun chatHistory(id: UUID): AiChatHistoryResponse
}

@Service
class AiServiceImpl(private val chatClient: ChatClient, private val chatMemory: ChatMemory) : AiService {
    override fun chat(request: AiChatRequest): Flux<AiChatResponse> {
        return chatClient.prompt()
            .user(request.prompt)
            .advisors {
                it.param(CHAT_MEMORY_CONVERSATION_ID_KEY, request.id.toString())
                it.param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, DEFAULT_CHAT_MEMORY_RESPONSE_SIZE)
            }
            .stream()
            .content()
            .map { AiChatResponse(request.id, it) }
    }

    override fun chatHistory(id: UUID): AiChatHistoryResponse {
        val messages = chatMemory.get(id.toString(), DEFAULT_CHAT_MEMORY_RESPONSE_SIZE)
            .map { AiChatMessage(it.content, it.messageType) }

        return AiChatHistoryResponse(id, messages)
    }
}
