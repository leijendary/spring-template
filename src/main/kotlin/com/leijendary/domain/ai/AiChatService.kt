package com.leijendary.domain.ai

import com.leijendary.context.RequestContext.userIdOrThrow
import com.leijendary.model.Cursorable
import com.leijendary.model.CursoredModel
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_RESPONSE_SIZE
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.concurrent.CompletableFuture.supplyAsync

interface AiChatService {
    fun cursor(cursorable: Cursorable): CursoredModel<AiChatResponse>
    fun create(request: AiChatRequest): Flux<AiChatCreateResponse>
    fun get(id: String): AiChatResponse
    fun history(id: String): AiChatHistoryResponse
}

@Service
class AiChatServiceImpl(
    private val aiChatRepository: AiChatRepository,
    private val chatClient: ChatClient,
    private val chatMemory: ChatMemory,
    private val titleChatClient: ChatClient,
) : AiChatService {
    override fun cursor(cursorable: Cursorable): CursoredModel<AiChatResponse> {
        val chats = aiChatRepository.cursor(userIdOrThrow, cursorable, AiChatResponse::class.java)

        return CursoredModel(chats, cursorable)
    }

    override fun create(request: AiChatRequest): Flux<AiChatCreateResponse> {
        val aiChat = if (!request.id.isNullOrBlank()) {
            aiChatRepository.findFirstByIdAndCreatedByOrThrow(request.id!!, userIdOrThrow, AiChat::class.java)
        } else {
            aiChatRepository.save(AiChat()).also { updateTitle(it, request.prompt) }
        }

        return chatClient.prompt()
            .user(request.prompt)
            .advisors {
                it.param(CHAT_MEMORY_CONVERSATION_ID_KEY, aiChat.id)
                it.param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, DEFAULT_CHAT_MEMORY_RESPONSE_SIZE)
            }
            .stream()
            .content()
            .map { AiChatCreateResponse(aiChat.id, it) }
    }

    override fun get(id: String): AiChatResponse {
        return aiChatRepository.findFirstByIdAndCreatedByOrThrow(id, userIdOrThrow, AiChatResponse::class.java)
    }

    override fun history(id: String): AiChatHistoryResponse {
        val messages = chatMemory.get(id, DEFAULT_CHAT_MEMORY_RESPONSE_SIZE)
            .map { AiChatMessage(it.content, it.messageType) }

        return AiChatHistoryResponse(id, messages)
    }

    private fun updateTitle(aiChat: AiChat, prompt: String) = supplyAsync {
        val title = titleChatClient.prompt()
            .user(prompt)
            .call()
            .content()

        if (!title.isNullOrBlank()) {
            aiChat.title = title

            aiChatRepository.save(aiChat)
        }
    }
}
