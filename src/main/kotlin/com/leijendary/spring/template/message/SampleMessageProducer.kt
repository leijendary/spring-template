package com.leijendary.spring.template.message

import com.leijendary.spring.template.api.v1.model.SampleMessage
import com.leijendary.spring.template.core.extension.AnyUtil.toJson
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

const val TOPIC_SAMPLE_CREATE = "leijendary.sample.create"
const val TOPIC_SAMPLE_UPDATE = "leijendary.sample.update"
const val TOPIC_SAMPLE_DELETE = "leijendary.sample.delete"

@Component
class SampleMessageProducer(private val kafkaTemplate: KafkaTemplate<String, String>) {
    fun create(sampleMessage: SampleMessage) {
        kafkaTemplate.send(TOPIC_SAMPLE_CREATE, sampleMessage.toJson())
    }

    fun update(sampleMessage: SampleMessage) {
        kafkaTemplate.send(TOPIC_SAMPLE_UPDATE, sampleMessage.toJson())
    }

    fun delete(sampleMessage: SampleMessage) {
        kafkaTemplate.send(TOPIC_SAMPLE_DELETE, sampleMessage.toJson())
    }
}
