package com.leijendary.spring.boot.template.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "retry")
class RetryProperties {
    var maxAttempts: Int = 3
    var backoff: Backoff = Backoff()

    inner class Backoff {
        var delay: Long = 50
        var maxDelay: Long = 200
        var multiplier: Double = 2.0
    }
}