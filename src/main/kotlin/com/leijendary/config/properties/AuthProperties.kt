package com.leijendary.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("auth")
data class AuthProperties(val system: System) {
    data class System(val principal: String)
}
