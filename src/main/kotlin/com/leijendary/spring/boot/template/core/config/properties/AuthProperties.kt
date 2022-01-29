package com.leijendary.spring.boot.template.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "auth")
class AuthProperties {
    var audience: String = ""
    var realm: String = ""
    var anonymousUser: AnonymousUser = AnonymousUser()
    var system: System = System()

    inner class AnonymousUser {
        var principal: String = ""
    }

    inner class System {
        var principal: String = ""
    }
}