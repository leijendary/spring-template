package com.leijendary.domain.ai.chat

import com.leijendary.context.RequestContext.userIdOrThrow
import com.leijendary.domain.ai.chat.AiChat.Companion.ENTITY
import com.leijendary.domain.ai.chat.AiChat.Companion.ERROR_SOURCE
import com.leijendary.domain.ai.chat.AiChatFunction.Companion.USER_ID_KEY
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.extension.logger
import com.leijendary.model.Cursorable
import com.leijendary.model.CursoredModel
import io.micrometer.tracing.Tracer
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.concurrent.CompletableFuture.supplyAsync

interface AiChatService {
    fun cursor(cursorable: Cursorable): CursoredModel<AiChatResponse>
    fun create(request: AiChatRequest): Flux<AiChatCreateResponse>
    fun get(id: String): AiChatResponse
    fun history(id: String): AiChatHistoryResponse
    fun delete(id: String)
}

@Service
class AiChatServiceImpl(
    private val aiChatRepository: AiChatRepository,
    private val chatClient: ChatClient,
    private val chatMemory: ChatMemory,
    private val titleChatClient: ChatClient,
    private val tracer: Tracer
) : AiChatService {
    private val log = logger()

    override fun cursor(cursorable: Cursorable): CursoredModel<AiChatResponse> {
        val chats = aiChatRepository.cursor(userIdOrThrow, cursorable).toMutableList()

        return CursoredModel(chats, cursorable)
    }

    override fun create(request: AiChatRequest): Flux<AiChatCreateResponse> {
        val aiChat = getOrCreate(request)

        return chatClient.prompt()
            .user(request.prompt)
            .system { it.param(USER_ID_KEY, userIdOrThrow) }
            .advisors { it.param(CONVERSATION_ID, aiChat.id) }
            .stream()
            .content()
            .map { AiChatCreateResponse(aiChat.id, it) }
    }

    override fun get(id: String): AiChatResponse {
        return aiChatRepository.findFirstByIdAndCreatedByOrThrow(id, userIdOrThrow, AiChatResponse::class.java)
    }

    override fun history(id: String): AiChatHistoryResponse {
        val exists = aiChatRepository.existsByIdAndCreatedBy(id, userIdOrThrow)

        if (!exists) {
            throw ResourceNotFoundException(id, ENTITY, ERROR_SOURCE)
        }

        val messages = chatMemory.get(id).map { AiChatMessage(it.text, it.messageType) }

        return AiChatHistoryResponse(id, messages)
    }

    override fun delete(id: String) {
        chatMemory.clear(id)

        log.info("Deleted $ENTITY history {}", id)

        aiChatRepository.deleteById(id)

        log.info("Deleted $ENTITY {}", id)
    }

    private fun getOrCreate(request: AiChatRequest): AiChat {
        if (!request.id.isNullOrBlank()) {
            return aiChatRepository.findFirstByIdAndCreatedByOrThrow(request.id, userIdOrThrow, AiChat::class.java)
        }

        val aiChat = aiChatRepository.save(AiChat())

        log.info("Created $ENTITY {}", aiChat.id)

        val span = tracer.currentSpan()

        supplyAsync {
            tracer.withSpan(span).use { updateTitle(aiChat, request.prompt) }
        }.exceptionally {
            log.error("Failed to update the chat title of {}", aiChat.id, it)
        }

        return aiChat
    }

    private fun updateTitle(aiChat: AiChat, prompt: String) {
        val title = titleChatClient.prompt()
            .user(prompt)
            .call()
            .content()

        if (!title.isNullOrBlank()) {
            aiChat.title = title

            aiChatRepository.save(aiChat)
        }

        log.info("Updated $ENTITY title of {} to {}", aiChat.id, title)
    }
}
