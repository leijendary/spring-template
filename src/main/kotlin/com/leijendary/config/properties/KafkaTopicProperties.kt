package com.leijendary.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.kafka.topic")
class KafkaTopicProperties {
    var sampleCreated = KafkaTopic()
    var sampleUpdated = KafkaTopic()
    var sampleDeleted = KafkaTopic()

    inner class KafkaTopic {
        lateinit var name: String
        var partitions = 1
        var replicas = 1
    }

    fun values(): List<KafkaTopic> {
        return listOf(sampleCreated, sampleUpdated, sampleDeleted)
    }
}
