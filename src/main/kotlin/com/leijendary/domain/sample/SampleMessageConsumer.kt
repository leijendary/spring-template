package com.leijendary.domain.sample

import com.leijendary.extension.toClass
import com.leijendary.model.IdentityModel
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SampleMessageConsumer(private val sampleSearchService: SampleSearchService) {
    @KafkaListener(
        topics = ["\${spring.kafka.topic.sampleCreated.name}"],
        concurrency = "\${spring.kafka.topic.sampleCreated.partitions}"
    )
    fun created(json: String) {
        val sample = json.toClass<SampleDetail>()

        sampleSearchService.save(sample)
    }

    @KafkaListener(
        topics = ["\${spring.kafka.topic.sampleUpdated.name}"],
        concurrency = "\${spring.kafka.topic.sampleUpdated.partitions}"
    )
    fun updated(json: String) {
        val sample = json.toClass<SampleDetail>()

        sampleSearchService.update(sample)
    }

    @KafkaListener(
        topics = ["\${spring.kafka.topic.sampleDeleted.name}"],
        concurrency = "\${spring.kafka.topic.sampleDeleted.partitions}"
    )
    fun deleted(json: String) {
        val model = json.toClass<IdentityModel>()

        sampleSearchService.delete(model.id)
    }
}
