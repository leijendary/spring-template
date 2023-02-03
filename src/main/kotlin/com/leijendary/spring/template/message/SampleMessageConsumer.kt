package com.leijendary.spring.template.message

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SampleMessageConsumer {
    @KafkaListener(topics = [TOPIC_SAMPLE_CREATE])
    fun created(json: String) {
    }

    @KafkaListener(topics = [TOPIC_SAMPLE_UPDATE])
    fun updated(json: String) {
    }

    @KafkaListener(topics = [TOPIC_SAMPLE_DELETE])
    fun deleted(json: String) {
    }
}
