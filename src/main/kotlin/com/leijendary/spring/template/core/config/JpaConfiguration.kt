package com.leijendary.spring.template.core.config

import com.leijendary.spring.template.core.util.RequestContext.now
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.Optional.of
import java.util.Optional.ofNullable

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", dateTimeProviderRef = "dateTimeProvider")
@EnableTransactionManagement
class JpaConfiguration {
    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAware {
            SecurityContextHolder
                .getContext()
                .let {
                    ofNullable(it.authentication?.name)
                }
        }
    }

    @Bean
    fun dateTimeProvider(): DateTimeProvider {
        return DateTimeProvider { of(now) }
    }
}
