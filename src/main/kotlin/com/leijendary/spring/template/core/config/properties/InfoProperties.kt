package com.leijendary.spring.template.core.config.properties

import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("info")
class InfoProperties {
    var app = App()
    var api = Api()

    class App {
        var organization: String = ""
        var group: String = ""
        var name: String = ""
        var description: String = ""
        var version: String = ""
    }

    class Api {
        var termsOfService: String = ""
        var contact: Contact? = null
        var license: License? = null
        var extensions: Map<String, Any> = HashMap()
    }
}
