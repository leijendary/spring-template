package com.leijendary.domain.ai

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.util.UUID

@RestController
@RequestMapping("api/v1/ai")
@Tag(name = "AI")
class AiController(private val aiService: AiService) {
    @PostMapping("chat")
    fun chat(@Valid @RequestBody request: AiChatRequest): Flux<AiChatResponse> {
        return aiService.chat(request)
    }

    @GetMapping("chat/{id}")
    fun chatHistory(@PathVariable id: UUID): AiChatHistoryResponse {
        return aiService.chatHistory(id)
    }
}
