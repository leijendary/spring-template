package com.leijendary.spring.template.event

import com.leijendary.spring.template.api.v1.mapper.SampleMapper
import com.leijendary.spring.template.api.v1.search.SampleSearch
import com.leijendary.spring.template.entity.SampleTable
import com.leijendary.spring.template.message.SampleMessageProducer
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class SampleTableEvent(
    private val sampleMessageProducer: SampleMessageProducer,
    private val sampleSearch: SampleSearch
) {
    companion object {
        private val MAPPER: SampleMapper = SampleMapper.INSTANCE
    }

    @Retryable
    fun create(sampleTable: SampleTable) {
        sampleSearch.save(sampleTable)

        // Create a message object
        val sampleMessage = MAPPER.toMessage(sampleTable)

        // Send the event object to kafka
        sampleMessageProducer.create(sampleMessage)
    }

    @Retryable
    fun update(sampleTable: SampleTable) {
        sampleSearch.update(sampleTable)

        // Create a message object
        val sampleMessage = MAPPER.toMessage(sampleTable)

        // Send the event object to kafka
        sampleMessageProducer.update(sampleMessage)
    }

    @Retryable
    fun delete(sampleTable: SampleTable) {
        sampleSearch.delete(sampleTable.id)

        // Create a message object
        val sampleMessage = MAPPER.toMessage(sampleTable)

        // Send the event object to kafka
        sampleMessageProducer.delete(sampleMessage)
    }
}
