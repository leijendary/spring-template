package com.leijendary.domain.ai

import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class AiService(private val chatModel: OllamaChatModel) {
    fun generate(query: String): Flux<String> {
        return chatModel.stream(query)
    }
}
