package com.leijendary.domain.sample

import com.leijendary.extension.toClass
import com.leijendary.model.IdentityModel
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SampleMessageConsumer(private val sampleSearchService: SampleSearchService) {
    @KafkaListener(topics = ["\${spring.kafka.topic.sampleCreated.name}"])
    fun created(json: String) {
        val sample = json.toClass<SampleDetail>()

        sampleSearchService.save(sample)
    }

    @KafkaListener(topics = ["\${spring.kafka.topic.sampleUpdated.name}"])
    fun updated(json: String) {
        val sample = json.toClass<SampleDetail>()

        sampleSearchService.update(sample)
    }

    @KafkaListener(topics = ["\${spring.kafka.topic.sampleDeleted.name}"])
    fun deleted(json: String) {
        val model = json.toClass<IdentityModel>()

        sampleSearchService.delete(model.id)
    }
}
