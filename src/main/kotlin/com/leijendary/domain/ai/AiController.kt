package com.leijendary.domain.ai

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("api/v1/ai")
@Tag(name = "AI")
class AiController(private val aiService: AiService) {
    @GetMapping
    fun generate(query: String): Flux<String> {
        return aiService.generate(query)
    }
}
