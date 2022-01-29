package com.leijendary.spring.boot.template.message

import com.leijendary.spring.boot.template.api.v1.data.SampleMessage
import com.leijendary.spring.boot.template.core.event.KafkaProducer
import org.springframework.stereotype.Component

@Component
class SampleMessageProducer(private val producer: KafkaProducer<SampleMessage>) {
    companion object {
        private const val BINDING_CREATE = "sampleCreate-out-0"
        private const val BINDING_UPDATE = "sampleUpdate-out-0"
        private const val BINDING_DELETE = "sampleDelete-out-0"
    }

    fun create(sampleMessage: SampleMessage) {
        producer.send(BINDING_CREATE, sampleMessage)
    }

    fun update(sampleMessage: SampleMessage) {
        producer.send(BINDING_UPDATE, sampleMessage)
    }

    fun delete(sampleMessage: SampleMessage) {
        producer.send(BINDING_DELETE, sampleMessage)
    }
}