package com.leijendary.spring.template.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.kafka.topic")
class KafkaTopicProperties {
    var sampleCreate = "sample.create"
    var sampleUpdate = "sample.update"
    var sampleDelete = "sample.delete"
}
