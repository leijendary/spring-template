package com.leijendary.domain.ai.chat

import com.leijendary.model.Cursorable
import com.leijendary.model.CursoredModel
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("api/v1/ai/chat")
@Tag(name = "AI Chat")
class AiChatController(private val aiChatService: AiChatService) {
    @GetMapping
    fun cursor(cursorable: Cursorable): CursoredModel<AiChatResponse> {
        return aiChatService.cursor(cursorable)
    }

    @PostMapping
    fun create(@Valid @RequestBody request: AiChatRequest): Flux<AiChatCreateResponse> {
        return aiChatService.create(request)
    }

    @GetMapping("{id}")
    fun get(@PathVariable id: String): AiChatResponse {
        return aiChatService.get(id)
    }

    @GetMapping("{id}/history")
    fun history(@PathVariable id: String): AiChatHistoryResponse {
        return aiChatService.history(id)
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    fun delete(@PathVariable id: String) {
        aiChatService.delete(id)
    }
}
