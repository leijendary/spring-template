package com.leijendary.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor
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
        tools: List<ToolContainer>,
        vectorStore: VectorStore
    ): ChatClient {
        val memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build()
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
            .defaultAdvisors(memoryAdvisor, ragAdvisor, loggerAdvisor)
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
