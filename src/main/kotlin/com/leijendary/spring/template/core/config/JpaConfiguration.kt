package com.leijendary.spring.template.core.config

import com.leijendary.spring.template.core.util.RequestContext.now
import com.leijendary.spring.template.core.util.RequestContext.userId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.Optional.of

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", dateTimeProviderRef = "dateTimeProvider")
@EnableTransactionManagement
class JpaConfiguration {
    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAware { of(userId) }
    }

    @Bean
    fun dateTimeProvider(): DateTimeProvider {
        return DateTimeProvider { of(now) }
    }
}
