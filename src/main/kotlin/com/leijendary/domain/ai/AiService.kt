package com.leijendary.domain.ai

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class AiService(private val chatClient: ChatClient) {
    fun generate(query: String): Flux<ChatResponse> {
        return chatClient.prompt()
            .user(query)
            .stream()
            .chatResponse()
    }
}
