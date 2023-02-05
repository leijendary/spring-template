package com.leijendary.spring.template.message

import com.leijendary.spring.template.api.v1.model.SampleMessage
import com.leijendary.spring.template.core.config.properties.KafkaTopicProperties
import com.leijendary.spring.template.core.extension.AnyUtil.toJson
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class SampleMessageProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val kafkaTopicProperties: KafkaTopicProperties
) {
    fun create(sampleMessage: SampleMessage) {
        kafkaTemplate.send(kafkaTopicProperties.sampleCreate, sampleMessage.toJson())
    }

    fun update(sampleMessage: SampleMessage) {
        kafkaTemplate.send(kafkaTopicProperties.sampleUpdate, sampleMessage.toJson())
    }

    fun delete(sampleMessage: SampleMessage) {
        kafkaTemplate.send(kafkaTopicProperties.sampleDelete, sampleMessage.toJson())
    }
}
