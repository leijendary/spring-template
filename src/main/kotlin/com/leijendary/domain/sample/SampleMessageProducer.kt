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
    fun created(sample: SampleDetailResponse) {
        kafkaTemplate.send(SAMPLE_CREATED, sample.toJson())
    }

    @Retryable
    fun updated(sample: SampleDetailResponse) {
        kafkaTemplate.send(SAMPLE_UPDATED, sample.toJson())
    }

    @Retryable
    fun deleted(id: String) {
        val model = IdentityModel(id)

        kafkaTemplate.send(SAMPLE_DELETED, model.toJson())
    }
}
