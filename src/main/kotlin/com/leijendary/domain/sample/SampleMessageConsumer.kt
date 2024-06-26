package com.leijendary.domain.sample

import com.leijendary.config.properties.Topic.SAMPLE_CREATED
import com.leijendary.config.properties.Topic.SAMPLE_DELETED
import com.leijendary.config.properties.Topic.SAMPLE_UPDATED
import com.leijendary.extension.toClass
import com.leijendary.model.IdentityModel
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SampleMessageConsumer(
    private val sampleRepository: SampleRepository,
    private val sampleSearchService: SampleSearchService
) {
    @KafkaListener(topics = [SAMPLE_CREATED])
    fun created(json: String) {
        val sample = json.toClass<SampleDetail>()
        sample.image = sampleRepository.getImage(sample.id)

        sampleSearchService.save(sample)
    }

    @KafkaListener(topics = [SAMPLE_UPDATED])
    fun updated(json: String) {
        val sample = json.toClass<SampleDetail>()

        sampleSearchService.update(sample)
    }

    @KafkaListener(topics = [SAMPLE_DELETED])
    fun deleted(json: String) {
        val model = json.toClass<IdentityModel>()

        sampleSearchService.delete(model.id)
    }
}
