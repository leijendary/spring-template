package com.leijendary.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.rag.retrieval.source.VectorStoreDocumentRetriever
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

@Configuration(proxyBeanMethods = false)
class AiConfiguration {
    @Value("classpath:prompts/general-instruction-system.st")
    private lateinit var generalInstructionSystem: Resource

    @Value("classpath:prompts/title-generator-system.st")
    private lateinit var titleGeneratorSystem: Resource

    @Bean
    fun chatClient(builder: ChatClient.Builder, vectorStore: VectorStore, chatMemory: ChatMemory): ChatClient {
        val memoryAdvisor = MessageChatMemoryAdvisor(chatMemory)
        val documentRetriever = VectorStoreDocumentRetriever.builder()
            .vectorStore(vectorStore)
            .build()
        val ragAdvisor = RetrievalAugmentationAdvisor.builder()
            .documentRetriever(documentRetriever)
            .build()

        return builder
            .defaultSystem(generalInstructionSystem)
            .defaultAdvisors(memoryAdvisor, ragAdvisor)
            .build()
    }

    @Bean
    fun titleChatClient(builder: ChatClient.Builder): ChatClient {
        return builder
            .defaultSystem(titleGeneratorSystem)
            .build()
    }
}
