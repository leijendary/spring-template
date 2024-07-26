package com.leijendary.domain.sample

import com.leijendary.config.properties.Topic.SAMPLE_CREATED
import com.leijendary.config.properties.Topic.SAMPLE_DELETED
import com.leijendary.config.properties.Topic.SAMPLE_UPDATED
import com.leijendary.extension.toClass
import com.leijendary.model.IdentityModel
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.stereotype.Component

@Component
class SampleMessageConsumer(private val sampleSearchService: SampleSearchService) {
    @KafkaListener(topics = [SAMPLE_CREATED])
    @RetryableTopic
    fun created(json: String) {
        val sample = json.toClass<SampleDetail>()

        sampleSearchService.save(sample)
    }

    @KafkaListener(topics = [SAMPLE_UPDATED])
    @RetryableTopic
    fun updated(json: String) {
        val sample = json.toClass<SampleDetail>()

        sampleSearchService.update(sample)
    }

    @KafkaListener(topics = [SAMPLE_DELETED])
    @RetryableTopic
    fun deleted(json: String) {
        val model = json.toClass<IdentityModel>()

        sampleSearchService.delete(model.id)
    }
}
