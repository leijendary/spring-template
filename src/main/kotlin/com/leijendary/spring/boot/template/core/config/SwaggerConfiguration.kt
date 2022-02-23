package com.leijendary.spring.boot.template.core.config

import com.leijendary.spring.boot.template.core.config.properties.InfoProperties
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfiguration(private val infoProperties: InfoProperties) {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(info())
    }

    private fun info(): Info {
        val app = infoProperties.app
        val api = infoProperties.api

        return Info()
            .title(app.name)
            .description(app.description)
            .termsOfService(api.termsOfService)
            .contact(api.contact)
            .license(api.license)
            .version(app.version)
    }
}