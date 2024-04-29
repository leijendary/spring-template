package com.leijendary.config.properties

import com.leijendary.config.properties.KafkaTopicProperties.KafkaTopic
import org.springframework.boot.context.properties.ConfigurationProperties

object Topic {
    const val SAMPLE_CREATED = "sampleCreated"
    const val SAMPLE_UPDATED = "sampleUpdated"
    const val SAMPLE_DELETED = "sampleDeleted"
}

@ConfigurationProperties("kafka.topic")
class KafkaTopicProperties : HashMap<String, KafkaTopic>() {
    data class KafkaTopic(val name: String, val partitions: Int = 1, val replicas: Int = 1)

    fun getName(key: String): String {
        return getValue(key).name
    }
}
