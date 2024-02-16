package com.leijendary.config

import com.leijendary.config.properties.OpenApiProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OpenApiProperties::class)
class OpenApiConfiguration
