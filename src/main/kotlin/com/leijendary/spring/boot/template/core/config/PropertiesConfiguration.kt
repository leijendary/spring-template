package com.leijendary.spring.boot.template.core.config

import com.leijendary.spring.boot.template.core.config.properties.*
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    AuthProperties::class,
    DataSourcePrimaryProperties::class,
    DataSourceReadonlyProperties::class,
    InfoProperties::class,
    NumberProperties::class,
    RetryProperties::class
)
class PropertiesConfiguration 