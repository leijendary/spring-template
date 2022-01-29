package com.leijendary.spring.boot.template.core.config

import com.leijendary.spring.boot.template.core.config.properties.InfoProperties
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfiguration(private val infoProperties: InfoProperties) {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(info())
            .schemaRequirement("Bearer", securityScheme())
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

    fun securityScheme(): SecurityScheme {
        return SecurityScheme()
            .type(HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
    }
}