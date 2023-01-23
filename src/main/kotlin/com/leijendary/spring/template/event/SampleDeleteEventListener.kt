package com.leijendary.spring.template.event

import com.leijendary.spring.template.api.v1.mapper.SampleMapper
import com.leijendary.spring.template.api.v1.search.SampleSearch
import com.leijendary.spring.template.model.SampleDeleteEvent
import com.leijendary.spring.template.message.SampleMessageProducer
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class SampleDeleteEventListener(
    private val sampleMessageProducer: SampleMessageProducer,
    private val sampleSearch: SampleSearch,
) {
    companion object {
        private val MAPPER: SampleMapper = SampleMapper.INSTANCE
    }

    @TransactionalEventListener
    @Async
    fun handleSampleDelete(sampleDeleteEvent: SampleDeleteEvent) {
        val sampleTable = sampleDeleteEvent.sampleTable
        val id = sampleTable.id

        // Delete the object from elasticsearch
        sampleSearch.delete(id)

        // Create a message object
        val sampleMessage = MAPPER.toMessage(sampleTable)

        // Send the event object to kafka
        sampleMessageProducer.delete(sampleMessage)
    }
}
