package com.leijendary.config

import com.leijendary.config.properties.*
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    AuthProperties::class,
    AwsCloudFrontProperties::class,
    AwsS3Properties::class,
    KafkaTopicProperties::class,
    NumberProperties::class,
    OpenApiProperties::class
)
class PropertiesConfiguration 
