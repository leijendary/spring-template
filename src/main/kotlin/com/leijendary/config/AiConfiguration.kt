package com.leijendary.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class AiConfiguration {
    @Bean
    fun chatClient(builder: ChatClient.Builder, vectorStore: VectorStore): ChatClient {
        val advisors = listOf(
            VectorStoreChatMemoryAdvisor(vectorStore),
            QuestionAnswerAdvisor(vectorStore),
            SimpleLoggerAdvisor()
        )

        return builder
            .defaultAdvisors(advisors)
            .build()
    }
}
