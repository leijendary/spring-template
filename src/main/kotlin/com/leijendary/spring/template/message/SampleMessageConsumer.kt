package com.leijendary.spring.template.message

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SampleMessageConsumer {
    @KafkaListener(topics = ["\${spring.kafka.topic.sampleCreate}"])
    fun created(json: String) {
    }

    @KafkaListener(topics = ["\${spring.kafka.topic.sampleUpdate}"])
    fun updated(json: String) {
    }

    @KafkaListener(topics = ["\${spring.kafka.topic.sampleDelete}"])
    fun deleted(json: String) {
    }
}
