package com.leijendary.config

import com.leijendary.config.properties.AuthProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AuthProperties::class)
class AuthConfiguration
