package com.leijendary.domain.sample

import com.leijendary.config.properties.Topic.SAMPLE_CREATED
import com.leijendary.config.properties.Topic.SAMPLE_DELETED
import com.leijendary.config.properties.Topic.SAMPLE_UPDATED
import com.leijendary.extension.toClass
import com.leijendary.model.IdentityModel
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SampleMessageConsumer(private val sampleSearchService: SampleSearchService) {
    @KafkaListener(
        topics = ["\${kafka.topic.$SAMPLE_CREATED.name}"],
        concurrency = "\${kafka.topic.$SAMPLE_CREATED.partitions}"
    )
    fun created(json: String) {
        val sample = json.toClass<SampleDetail>()

        sampleSearchService.save(sample)
    }

    @KafkaListener(
        topics = ["\${kafka.topic.$SAMPLE_UPDATED.name}"],
        concurrency = "\${kafka.topic.$SAMPLE_UPDATED.partitions}"
    )
    fun updated(json: String) {
        val sample = json.toClass<SampleDetail>()

        sampleSearchService.update(sample)
    }

    @KafkaListener(
        topics = ["\${kafka.topic.$SAMPLE_DELETED.name}"],
        concurrency = "\${kafka.topic.$SAMPLE_DELETED.partitions}"
    )
    fun deleted(json: String) {
        val model = json.toClass<IdentityModel>()

        sampleSearchService.delete(model.id)
    }
}
