package com.leijendary.spring.boot.template.core.config

import com.leijendary.spring.boot.template.core.config.properties.AuthProperties
import com.leijendary.spring.boot.template.core.util.RequestContext.now
import com.leijendary.spring.boot.template.core.util.RequestContext.userId
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
class JpaConfiguration(private val authProperties: AuthProperties) {

    @Bean
    fun auditorProvider(): AuditorAware<String> {
        val anonymousUser = authProperties.anonymousUser.principal

        return AuditorAware { of(userId ?: anonymousUser) }
    }

    @Bean
    fun dateTimeProvider(): DateTimeProvider {
        return DateTimeProvider { of(now) }
    }
}