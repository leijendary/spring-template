package com.leijendary.spring.template.message

import com.leijendary.spring.template.api.v1.mapper.SampleMapper
import com.leijendary.spring.template.api.v1.model.SampleMessage
import com.leijendary.spring.template.api.v1.search.SampleSearch
import com.leijendary.spring.template.core.extension.toClass
import com.leijendary.spring.template.entity.SampleTable
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SampleMessageConsumer(private val sampleSearch: SampleSearch) {
    companion object {
        private val MAPPER = SampleMapper.INSTANCE
    }

    @KafkaListener(topics = ["\${spring.kafka.topic.sampleCreate}"])
    fun created(json: String) {
        val sampleTable = toEntity(json)

        sampleSearch.save(sampleTable)
    }

    @KafkaListener(topics = ["\${spring.kafka.topic.sampleUpdate}"])
    fun updated(json: String) {
        val sampleTable = toEntity(json)

        sampleSearch.update(sampleTable)
    }

    @KafkaListener(topics = ["\${spring.kafka.topic.sampleDelete}"])
    fun deleted(json: String) {
        val sampleTable = toEntity(json)

        sampleSearch.delete(sampleTable.id)
    }

    private fun toEntity(json: String): SampleTable {
        val message = json.toClass(SampleMessage::class)

        return MAPPER.toEntity(message)
    }
}
