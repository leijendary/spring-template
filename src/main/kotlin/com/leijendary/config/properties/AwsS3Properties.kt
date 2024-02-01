package com.leijendary.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("spring.cloud.aws.s3")
data class AwsS3Properties(val bucketName: String, val signatureDuration: Duration = Duration.ofMinutes(30))
