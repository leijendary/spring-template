package com.leijendary.spring.template.core.config

import com.leijendary.spring.template.core.config.properties.*
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    AuthProperties::class,
    AwsS3Properties::class,
    DataSourcePrimaryProperties::class,
    DataSourceReadonlyProperties::class,
    EmissionProperties::class,
    InfoProperties::class,
    NumberProperties::class,
    RetryProperties::class
)
class PropertiesConfiguration 
