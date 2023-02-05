package com.leijendary.spring.template.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.cloud.aws.s3")
class AwsS3Properties {
    var bucketName: String = ""
}
