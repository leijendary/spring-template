package com.leijendary.spring.boot.template.config;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leijendary.spring.boot.template.json.serializer.OffsetDateTimeToEpochMillisSerializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JacksonConfiguration {

    @Bean
    public JavaTimeModule javaTimeModule() {
        final var module = new JavaTimeModule();
        module.addSerializer(OffsetDateTime.class, new OffsetDateTimeToEpochMillisSerializer());

        return module;
    }
}
