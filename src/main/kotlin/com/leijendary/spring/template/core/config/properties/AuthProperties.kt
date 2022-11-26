package com.leijendary.spring.template.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "auth")
class AuthProperties {
    var anonymousUser: AnonymousUser = AnonymousUser()
    var system: System = System()

    class AnonymousUser {
        var principal: String = ""
    }

    class System {
        var principal: String = ""
    }
}