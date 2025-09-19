package com.leijendary.domain.sample

import com.leijendary.config.properties.Topic.SAMPLE_CREATED
import com.leijendary.config.properties.Topic.SAMPLE_DELETED
import com.leijendary.config.properties.Topic.SAMPLE_UPDATED
import com.leijendary.extension.toJson
import com.leijendary.model.IdentityModel
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class SampleMessageProducer(private val kafkaTemplate: KafkaTemplate<String, String>) {
    @Retryable
    fun created(sample: Sample) {
        val message = SampleMapperImpl.toMessage(sample)

        kafkaTemplate.send(SAMPLE_CREATED, message.toJson())
    }

    @Retryable
    fun updated(sample: Sample) {
        val message = SampleMapperImpl.toMessage(sample)

        kafkaTemplate.send(SAMPLE_UPDATED, message.toJson())
    }

    @Retryable
    fun deleted(id: String) {
        val model = IdentityModel(id)

        kafkaTemplate.send(SAMPLE_DELETED, model.toJson())
    }
}
