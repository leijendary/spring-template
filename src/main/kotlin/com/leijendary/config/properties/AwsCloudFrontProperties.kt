package com.leijendary.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("spring.cloud.aws.cloud-front")
@JvmRecord
data class AwsCloudFrontProperties(
    val distributionId: String,
    val url: String,
    val publicKeyId: String,
    val privateKey: String,
    val signatureDuration: Duration = Duration.ofMinutes(30)
)
