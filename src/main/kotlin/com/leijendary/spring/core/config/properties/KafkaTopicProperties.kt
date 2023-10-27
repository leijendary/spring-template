package com.leijendary.spring.core.config.properties

import com.leijendary.spring.core.config.properties.KafkaTopicProperties.KafkaTopic
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.kafka.topic")
class KafkaTopicProperties : HashMap<String, KafkaTopic>() {
    class KafkaTopic {
        lateinit var name: String
        var partitions: Int = 1
        var replicas: Int = 1
    }

    fun nameOf(key: String): String {
        return this[key]!!.name
    }
}
