package com.leijendary.spring.boot.template.core.config;

import static com.leijendary.spring.boot.template.core.util.RequestContext.now;
import static java.util.Optional.of;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
public class JpaConfiguration {

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> of(now());
    }
}
