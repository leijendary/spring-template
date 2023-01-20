package com.leijendary.spring.template.core.config

import com.leijendary.spring.template.core.config.properties.InfoProperties
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

const val SECURITY_SCHEME_BEARER = "Bearer"

@Configuration
@SecurityScheme(
    name = SECURITY_SCHEME_BEARER,
    type = HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
class OpenAPIConfiguration(private val infoProperties: InfoProperties) {
    @Bean
    fun openAPI(): OpenAPI {
        val app = infoProperties.app
        val api = infoProperties.api
        val info = Info().apply {
            title = app.name
            description = app.description
            termsOfService = api.termsOfService
            contact = api.contact
            license = api.license
            version = app.version
        }

        return OpenAPI().info(info)
    }
}
