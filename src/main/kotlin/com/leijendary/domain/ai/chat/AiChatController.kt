package com.leijendary.domain.ai.chat

import com.leijendary.model.Cursorable
import com.leijendary.model.CursoredModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RestController
@RequestMapping("api/v1/ai/chat")
@Tag(name = "AI Chat")
class AiChatController(private val aiChatService: AiChatService) {
    @GetMapping
    @Operation(
        description = "Get the list of AI chat history.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["ai.chat.read"])]
    )
    fun cursor(cursorable: Cursorable): CursoredModel<AiChatResponse> {
        return aiChatService.cursor(cursorable)
    }

    @PostMapping
    @Operation(
        description = "Start a new AI chat session or send to the previous chat history by passing the ID.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["ai.chat.write"])]
    )
    fun create(@Valid @RequestBody request: AiChatRequest): Flux<AiChatCreateResponse> {
        return aiChatService.create(request)
    }

    @GetMapping("{id}")
    @Operation(
        description = "Get the details of an existing AI chat.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["ai.chat.read"])]
    )
    fun get(@PathVariable id: String): AiChatResponse {
        return aiChatService.get(id)
    }

    @GetMapping("{id}/history")
    @Operation(
        description = "Get the history of an AI chat.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["ai.chat.read"])]
    )
    fun history(@PathVariable id: String): AiChatHistoryResponse {
        return aiChatService.history(id)
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        description = "Delete an existing AI chat.",
        security = [SecurityRequirement(name = "oauth2", scopes = ["ai.chat.write"])]
    )
    fun delete(@PathVariable id: String) {
        aiChatService.delete(id)
    }
}
