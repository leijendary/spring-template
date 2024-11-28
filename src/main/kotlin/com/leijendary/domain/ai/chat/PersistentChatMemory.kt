package com.leijendary.domain.ai.chat

import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.MessageType
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class PersistentChatMemory(private val aiChatMemoryRepository: AiChatMemoryRepository) : ChatMemory {
    override fun add(conversationId: String, messages: List<Message>) {
        val chats = messages.map { AiChatMemory(conversationId, it.content, it.messageType) }

        aiChatMemoryRepository.saveAll(chats)
    }

    override fun get(conversationId: String, lastN: Int): List<Message> {
        val pageable = PageRequest.of(0, lastN, SORT_DEFAULT)
        val page = aiChatMemoryRepository.findBySessionId(conversationId, pageable)

        return page.mapNotNull {
            when (it.type) {
                MessageType.USER -> UserMessage(it.content, emptyList(), createMetadata(it))
                MessageType.ASSISTANT -> AssistantMessage(it.content, createMetadata(it))
                else -> null
            }
        }
    }

    override fun clear(conversationId: String) {
        aiChatMemoryRepository.deleteById(conversationId)
    }

    private fun createMetadata(memory: AiChatMemory): Map<String, Any> {
        return mapOf("timestamp" to memory.timestamp)
    }

    companion object {
        private val SORT_DEFAULT = Sort.by(Sort.Direction.DESC, "timestamp")
    }
}
