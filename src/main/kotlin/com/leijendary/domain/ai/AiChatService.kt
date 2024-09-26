package com.leijendary.domain.ai

import com.leijendary.context.RequestContext.userIdOrThrow
import com.leijendary.domain.ai.AiChat.Companion.ENTITY
import com.leijendary.domain.ai.AiChat.Companion.ERROR_SOURCE
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.model.Cursorable
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_RESPONSE_SIZE
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.concurrent.CompletableFuture.supplyAsync

interface AiChatService {
    fun list(cursorable: Cursorable): List<AiChatResponse>
    fun create(request: AiChatRequest): Flux<AiChatCreateResponse>
    fun get(id: Long): AiChatResponse
    fun history(id: Long): AiChatHistoryResponse
}

@Service
class AiChatServiceImpl(
    private val aiChatRepository: AiChatRepository,
    private val chatClient: ChatClient,
    private val chatMemory: ChatMemory,
    private val titleChatClient: ChatClient,
) : AiChatService {
    override fun list(cursorable: Cursorable): List<AiChatResponse> {
        return aiChatRepository.cursor(userIdOrThrow, cursorable, AiChatResponse::class.java)
    }

    override fun create(request: AiChatRequest): Flux<AiChatCreateResponse> {
        val aiChat = if (request.id <= 0) {
            aiChatRepository.save(AiChat()).also { updateTitle(it, request.prompt) }
        } else {
            aiChatRepository.findFirstByIdAndCreatedBy(request.id, userIdOrThrow, AiChat::class.java)
                .orElseThrow { ResourceNotFoundException(request.id, ENTITY, ERROR_SOURCE) }
        }

        return chatClient.prompt()
            .user(request.prompt)
            .advisors {
                it.param(CHAT_MEMORY_CONVERSATION_ID_KEY, aiChat.id.toString())
                it.param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, DEFAULT_CHAT_MEMORY_RESPONSE_SIZE)
            }
            .stream()
            .content()
            .map { AiChatCreateResponse(aiChat.id, it) }
    }

    override fun get(id: Long): AiChatResponse {
        return aiChatRepository.findFirstByIdAndCreatedBy(id, userIdOrThrow, AiChatResponse::class.java)
            .orElseThrow { ResourceNotFoundException(id, ENTITY, ERROR_SOURCE) }
    }

    override fun history(id: Long): AiChatHistoryResponse {
        val messages = chatMemory.get(id.toString(), DEFAULT_CHAT_MEMORY_RESPONSE_SIZE)
            .map { AiChatMessage(it.content, it.messageType) }

        return AiChatHistoryResponse(id, messages)
    }

    private fun updateTitle(aiChat: AiChat, prompt: String) = supplyAsync {
        aiChat.title = titleChatClient.prompt()
            .user(prompt)
            .call()
            .content()

        aiChatRepository.save(aiChat)
    }
}
