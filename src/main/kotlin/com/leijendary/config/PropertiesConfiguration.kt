package com.leijendary.config

import com.leijendary.config.properties.*
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(
    AuthProperties::class,
    AwsCloudFrontProperties::class,
    AwsS3Properties::class,
    DataSourcePrimaryProperties::class,
    DataSourceReadOnlyProperties::class,
    KafkaTopicProperties::class,
    NumberProperties::class,
    OpenApiProperties::class
)
class PropertiesConfiguration 
