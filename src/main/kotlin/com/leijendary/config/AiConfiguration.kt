package com.leijendary.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
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
        return builder
            .defaultSystem(generalInstructionSystem)
            .defaultAdvisors(
                PromptChatMemoryAdvisor(chatMemory),
                QuestionAnswerAdvisor(vectorStore),
            )
            .build()
    }

    @Bean
    fun titleChatClient(builder: ChatClient.Builder): ChatClient {
        return builder
            .defaultSystem(titleGeneratorSystem)
            .build()
    }
}
