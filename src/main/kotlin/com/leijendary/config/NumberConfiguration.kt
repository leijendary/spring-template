package com.leijendary.config

import com.leijendary.config.properties.NumberProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(NumberProperties::class)
class NumberConfiguration
