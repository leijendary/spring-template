package com.leijendary.spring.template.event

import com.leijendary.spring.template.api.v1.mapper.SampleMapper
import com.leijendary.spring.template.api.v1.search.SampleSearch
import com.leijendary.spring.template.model.SampleCreateEvent
import com.leijendary.spring.template.message.SampleMessageProducer
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class SampleCreateEventListener(
    private val sampleMessageProducer: SampleMessageProducer,
    private val sampleSearch: SampleSearch,
) {
    companion object {
        private val MAPPER: SampleMapper = SampleMapper.INSTANCE
    }

    @TransactionalEventListener
    @Async
    fun handleSampleCreate(sampleCreateEvent: SampleCreateEvent) {
        val sampleTable = sampleCreateEvent.sampleTable

        // Save the object to elasticsearch
        sampleSearch.save(sampleTable)

        // Create a message object
        val sampleMessage = MAPPER.toMessage(sampleTable)

        // Send the event object to kafka
        sampleMessageProducer.create(sampleMessage)
    }
}
