package com.leijendary.spring.boot.template.core.config.properties

import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "info")
class InfoProperties {
    var app: App = App()
    var api: Api = Api()

    inner class App {
        var organization: String = ""
        var group: String = ""
        var name: String = ""
        var description: String = ""
        var version: String = ""
    }

    inner class Api {
        var termsOfService: String = ""
        var contact: Contact? = null
        var license: License? = null
        var extensions: Map<String, Any> = HashMap()
    }
}