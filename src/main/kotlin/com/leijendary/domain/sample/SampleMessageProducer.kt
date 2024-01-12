package com.leijendary.domain.sample

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
        kafkaTemplate.send(kafkaTopicProperties.sampleCreated.name, sample.toJson())
    }

    @Retryable
    fun updated(sample: SampleDetail) {
        kafkaTemplate.send(kafkaTopicProperties.sampleUpdated.name, sample.toJson())
    }

    @Retryable
    fun deleted(id: Long) {
        val model = IdentityModel(id)

        kafkaTemplate.send(kafkaTopicProperties.sampleDeleted.name, model.toJson())
    }
}
