package com.leijendary.spring.boot.template.core.config

import com.leijendary.spring.boot.template.core.config.properties.AuthProperties
import com.leijendary.spring.boot.template.core.config.properties.DataSourcePrimaryProperties
import com.leijendary.spring.boot.template.core.config.properties.DataSourceReadonlyProperties
import com.leijendary.spring.boot.template.core.config.properties.InfoProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    AuthProperties::class,
    DataSourcePrimaryProperties::class,
    DataSourceReadonlyProperties::class,
    InfoProperties::class
)
class PropertiesConfiguration 