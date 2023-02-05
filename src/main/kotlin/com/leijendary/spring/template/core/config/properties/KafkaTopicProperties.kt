package com.leijendary.spring.template.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.kafka.topic")
class KafkaTopicProperties {
    var sampleCreate: String = "sample.create"
    var sampleUpdate: String = "sample.update"
    var sampleDelete: String = "sample.delete"
}
