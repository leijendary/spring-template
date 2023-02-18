package com.leijendary.spring.template.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("auth")
class AuthProperties {
    var system = System()

    inner class System {
        var principal: String = ""
    }
}
