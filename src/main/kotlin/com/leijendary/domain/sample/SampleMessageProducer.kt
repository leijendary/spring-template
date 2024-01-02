package com.leijendary.domain.sample

import com.leijendary.config.Topic.SAMPLE_CREATED
import com.leijendary.config.Topic.SAMPLE_DELETED
import com.leijendary.config.Topic.SAMPLE_UPDATED
import com.leijendary.config.properties.KafkaTopicProperties
import com.leijendary.extension.toJson
import com.leijendary.model.IdentityModel
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class SampleMessageProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val kafkaTopicProperties: KafkaTopicProperties
) {
    @Retryable
    fun created(sample: SampleDetail) {
        val topic = kafkaTopicProperties.nameOf(SAMPLE_CREATED)

        kafkaTemplate.send(topic, sample.toJson())
    }

    @Retryable
    fun updated(sample: SampleDetail) {
        val topic = kafkaTopicProperties.nameOf(SAMPLE_UPDATED)

        kafkaTemplate.send(topic, sample.toJson())
    }

    @Retryable
    fun deleted(id: Long) {
        val topic = kafkaTopicProperties.nameOf(SAMPLE_DELETED)
        val model = IdentityModel(id)

        kafkaTemplate.send(topic, model.toJson())
    }
}
