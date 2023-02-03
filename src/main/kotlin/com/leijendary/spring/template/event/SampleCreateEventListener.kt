package com.leijendary.spring.template.event

import com.leijendary.spring.template.api.v1.mapper.SampleMapper
import com.leijendary.spring.template.api.v1.search.SampleSearch
import com.leijendary.spring.template.message.SampleMessageProducer
import com.leijendary.spring.template.model.SampleCreateEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase.AFTER_COMPLETION
import org.springframework.transaction.event.TransactionalEventListener

@Component
class SampleCreateEventListener(
    private val sampleMessageProducer: SampleMessageProducer,
    private val sampleSearch: SampleSearch,
) {
    companion object {
        private val MAPPER: SampleMapper = SampleMapper.INSTANCE
    }

    @Retryable
    @TransactionalEventListener(phase = AFTER_COMPLETION)
    fun handle(sampleCreateEvent: SampleCreateEvent) = runBlocking {
        val sampleTable = sampleCreateEvent.sampleTable

        launch {
            // Save the object to elasticsearch
            sampleSearch.save(sampleTable)
        }

        launch {
            // Create a message object
            val sampleMessage = MAPPER.toMessage(sampleTable)

            // Send the event object to kafka
            sampleMessageProducer.create(sampleMessage)
        }
    }
}
