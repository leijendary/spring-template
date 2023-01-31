package com.leijendary.spring.template.core.config

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfiguration {
    @Bean
    fun javaTimeModule() = JavaTimeModule()
}
