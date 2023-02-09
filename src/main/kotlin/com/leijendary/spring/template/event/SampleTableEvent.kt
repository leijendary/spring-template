package com.leijendary.spring.template.event

import com.leijendary.spring.template.api.v1.mapper.SampleMapper
import com.leijendary.spring.template.api.v1.search.SampleSearch
import com.leijendary.spring.template.entity.SampleTable
import com.leijendary.spring.template.message.SampleMessageProducer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class SampleTableEvent(
    private val sampleMessageProducer: SampleMessageProducer,
    private val sampleSearch: SampleSearch,
) {
    companion object {
        private val MAPPER: SampleMapper = SampleMapper.INSTANCE
    }

    @Retryable
    suspend fun create(sampleTable: SampleTable) = coroutineScope {
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

    @Retryable
    suspend fun update(sampleTable: SampleTable) = coroutineScope {
        launch {
            // Update the object from elasticsearch
            sampleSearch.update(sampleTable)
        }

        launch {
            // Create a message object
            val sampleMessage = MAPPER.toMessage(sampleTable)

            // Send the event object to kafka
            sampleMessageProducer.update(sampleMessage)
        }
    }

    @Retryable
    suspend fun delete(sampleTable: SampleTable) = coroutineScope {
        val id = sampleTable.id

        launch {
            // Delete the object from elasticsearch
            sampleSearch.delete(id)
        }

        launch {
            // Create a message object
            val sampleMessage = MAPPER.toMessage(sampleTable)

            // Send the event object to kafka
            sampleMessageProducer.delete(sampleMessage)
        }
    }
}
