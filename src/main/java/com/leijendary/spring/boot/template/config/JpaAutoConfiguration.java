package com.leijendary.spring.boot.template.config;

import static java.time.OffsetDateTime.now;
import static java.util.Optional.of;

import javax.persistence.EntityManager;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@ConditionalOnClass(EntityManager.class)
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
public class JpaAutoConfiguration {

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> of(now());
    }
}
