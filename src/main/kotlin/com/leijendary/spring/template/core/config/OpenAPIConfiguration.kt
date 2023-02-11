package com.leijendary.spring.template.core.config

import com.leijendary.spring.template.core.config.properties.InfoProperties
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType.APIKEY
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

const val HEADER_USER_ID = "X-User-ID"

@Configuration
@SecurityScheme(name = HEADER_USER_ID, type = APIKEY, `in` = HEADER)
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
