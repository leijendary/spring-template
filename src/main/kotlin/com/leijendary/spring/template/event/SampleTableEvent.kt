package com.leijendary.spring.template.event

import com.leijendary.spring.template.api.v1.mapper.SampleMapper
import com.leijendary.spring.template.entity.SampleTable
import com.leijendary.spring.template.message.SampleMessageProducer
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class SampleTableEvent(private val sampleMessageProducer: SampleMessageProducer) {
    companion object {
        private val MAPPER = SampleMapper.INSTANCE
    }

    @Retryable
    fun create(sampleTable: SampleTable) {
        // Create a message object
        val sampleMessage = MAPPER.toMessage(sampleTable)

        // Send the event object to kafka
        sampleMessageProducer.create(sampleMessage)
    }

    @Retryable
    fun update(sampleTable: SampleTable) {
        // Create a message object
        val sampleMessage = MAPPER.toMessage(sampleTable)

        // Send the event object to kafka
        sampleMessageProducer.update(sampleMessage)
    }

    @Retryable
    fun delete(sampleTable: SampleTable) {
        // Create a message object
        val sampleMessage = MAPPER.toMessage(sampleTable)

        // Send the event object to kafka
        sampleMessageProducer.delete(sampleMessage)
    }
}
