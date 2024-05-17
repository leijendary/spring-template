package com.leijendary.config.properties

import io.swagger.v3.oas.models.OpenAPI
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("open-api", ignoreUnknownFields = false)
class OpenApiProperties : OpenAPI()
