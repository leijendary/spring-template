package com.leijendary.config.properties

import com.leijendary.config.properties.KafkaTopicProperties.KafkaTopic
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.kafka.topic")
class KafkaTopicProperties : HashMap<String, KafkaTopic>() {
    class KafkaTopic {
        lateinit var name: String
        var partitions = 1
        var replicas = 1
    }

    fun nameOf(key: String): String {
        return getValue(key).name
    }
}
