package com.leijendary.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.kafka.topic")
data class KafkaTopicProperties(
    val sampleCreated: KafkaTopic,
    val sampleUpdated: KafkaTopic,
    val sampleDeleted: KafkaTopic,
) {
    @JvmRecord
    data class KafkaTopic(val name: String, val partitions: Int = 1, val replicas: Int = 1)

    fun values(): List<KafkaTopic> {
        return listOf(sampleCreated, sampleUpdated, sampleDeleted)
    }
}
