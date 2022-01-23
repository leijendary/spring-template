package com.leijendary.spring.boot.template.core.config;

import static com.leijendary.spring.boot.template.core.util.RequestContext.getUsername;
import static com.leijendary.spring.boot.template.core.util.RequestContext.now;
import static java.util.Optional.of;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", dateTimeProviderRef = "dateTimeProvider")
@EnableTransactionManagement
public class JpaConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> of(getUsername());
    }

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> of(now());
    }
}
