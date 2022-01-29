package com.leijendary.spring.boot.template.core.config

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.leijendary.spring.boot.template.core.json.OffsetDateTimeToEpochMillisSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.OffsetDateTime

@Configuration
class JacksonConfiguration {
    @Bean
    fun javaTimeModule(): JavaTimeModule {
        val module = JavaTimeModule()
        module.addSerializer(OffsetDateTime::class.java, OffsetDateTimeToEpochMillisSerializer())

        return module
    }
}