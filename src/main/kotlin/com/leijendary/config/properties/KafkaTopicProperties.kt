package com.leijendary.config.properties

import com.leijendary.config.properties.KafkaTopicProperties.KafkaTopic
import org.springframework.boot.context.properties.ConfigurationProperties

object Topic {
    const val SAMPLE_CREATED = "sample.created"
    const val SAMPLE_UPDATED = "sample.updated"
    const val SAMPLE_DELETED = "sample.deleted"
}

@ConfigurationProperties("kafka.topic", ignoreUnknownFields = false)
class KafkaTopicProperties : HashMap<String, KafkaTopic>() {
    data class KafkaTopic(val partitions: Int = 1)
}
