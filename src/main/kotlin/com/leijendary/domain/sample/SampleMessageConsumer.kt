package com.leijendary.domain.sample

import com.leijendary.extension.toClass
import com.leijendary.model.IdentityModel
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SampleMessageConsumer(private val sampleSearchService: SampleSearchService) {
    @KafkaListener(
        topics = ["\${kafka.topic.sampleCreated.name}"],
        concurrency = "\${kafka.topic.sampleCreated.partitions}"
    )
    fun created(json: String) {
        val sample = json.toClass<SampleDetail>()

        sampleSearchService.save(sample)
    }

    @KafkaListener(
        topics = ["\${kafka.topic.sampleUpdated.name}"],
        concurrency = "\${kafka.topic.sampleUpdated.partitions}"
    )
    fun updated(json: String) {
        val sample = json.toClass<SampleDetail>()

        sampleSearchService.update(sample)
    }

    @KafkaListener(
        topics = ["\${kafka.topic.sampleDeleted.name}"],
        concurrency = "\${kafka.topic.sampleDeleted.partitions}"
    )
    fun deleted(json: String) {
        val model = json.toClass<IdentityModel>()

        sampleSearchService.delete(model.id)
    }
}
