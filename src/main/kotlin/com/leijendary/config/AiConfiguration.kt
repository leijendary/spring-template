package com.leijendary.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_RESPONSE_SIZE
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

interface ToolContainer

@Configuration(proxyBeanMethods = false)
class AiConfiguration {
    @Value("classpath:prompts/general-instruction-system.txt")
    private lateinit var generalInstructionSystem: Resource

    @Value("classpath:prompts/title-generator-system.txt")
    private lateinit var titleGeneratorSystem: Resource

    @Bean
    fun chatClient(
        builder: ChatClient.Builder,
        chatMemory: ChatMemory,
        vectorStore: VectorStore,
        tools: List<ToolContainer>
    ): ChatClient {
        val memoryAdvisor = MessageChatMemoryAdvisor(chatMemory)
        val documentRetriever = VectorStoreDocumentRetriever.builder()
            .vectorStore(vectorStore)
            .build()
        val queryAugmenter = ContextualQueryAugmenter.builder()
            .allowEmptyContext(true)
            .build()
        val ragAdvisor = RetrievalAugmentationAdvisor.builder()
            .documentRetriever(documentRetriever)
            .queryAugmenter(queryAugmenter)
            .build()
        val loggerAdvisor = SimpleLoggerAdvisor()

        return builder
            .defaultSystem(generalInstructionSystem)
            .defaultAdvisors {
                it.advisors(memoryAdvisor, ragAdvisor, loggerAdvisor)
                it.param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, DEFAULT_CHAT_MEMORY_RESPONSE_SIZE)
            }
            .defaultTools(*tools.toTypedArray())
            .build()
    }

    @Bean
    fun titleChatClient(builder: ChatClient.Builder): ChatClient {
        return builder
            .defaultSystem(titleGeneratorSystem)
            .build()
    }
}
