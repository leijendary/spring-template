package com.leijendary.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("aws.s3", ignoreUnknownFields = false)
data class AwsS3Properties(val bucketName: String, val signatureDuration: Duration = Duration.ofMinutes(30))
