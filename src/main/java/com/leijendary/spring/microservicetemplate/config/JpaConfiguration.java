package com.leijendary.spring.microservicetemplate.config;

import com.leijendary.spring.microservicetemplate.repository.provider.LocaleAwareRepositoryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.leijendary.spring.microservicetemplate.util.RequestContextUtil.now;
import static java.util.Optional.of;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.leijendary.spring.microservicetemplate.repository",
        repositoryBaseClass = LocaleAwareRepositoryProvider.class)
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
public class JpaConfiguration {

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> of(now());
    }
}
