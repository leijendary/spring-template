package com.leijendary.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class AiConfiguration {
    @Bean
    fun chatClient(builder: ChatClient.Builder, vectorStore: VectorStore, chatMemory: ChatMemory): ChatClient {
        val advisors = listOf(
            PromptChatMemoryAdvisor(chatMemory),
            QuestionAnswerAdvisor(vectorStore),
            SimpleLoggerAdvisor()
        )

        return builder
            .defaultAdvisors(advisors)
            .build()
    }
}
