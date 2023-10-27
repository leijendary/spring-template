package com.leijendary.spring.core.config.properties

import io.swagger.v3.oas.models.OpenAPI
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("open-api")
class OpenApiProperties : OpenAPI()
