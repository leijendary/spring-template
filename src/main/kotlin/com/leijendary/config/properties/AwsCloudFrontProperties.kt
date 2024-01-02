package com.leijendary.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("spring.cloud.aws.cloud-front")
class AwsCloudFrontProperties {
    lateinit var distributionId: String
    lateinit var url: String
    lateinit var publicKeyId: String
    lateinit var privateKey: String
    var signatureDuration: Duration = Duration.ofMinutes(30)
}
