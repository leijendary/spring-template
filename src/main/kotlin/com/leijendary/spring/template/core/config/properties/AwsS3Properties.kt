package com.leijendary.spring.template.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("spring.cloud.aws.s3")
class AwsS3Properties {
    var bucketName: String = ""
    var signatureDuration: Duration = Duration.ofMinutes(30)
}
