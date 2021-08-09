package com.leijendary.spring.microservicetemplate.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leijendary.spring.microservicetemplate.json.serializer.OffsetDateTimeToEpochMillisSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;

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
