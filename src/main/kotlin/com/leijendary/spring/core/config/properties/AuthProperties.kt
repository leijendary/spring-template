package com.leijendary.spring.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("auth")
class AuthProperties {
    var system = System()

    inner class System {
        lateinit var principal: String
    }
}
