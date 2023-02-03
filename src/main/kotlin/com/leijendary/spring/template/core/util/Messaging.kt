package com.leijendary.spring.template.core.util

import org.springframework.kafka.support.KafkaHeaders.KEY
import org.springframework.kafka.support.KafkaHeaders.PARTITION
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder

object Messaging {
    fun <V> create(value: V, key: String? = null, partition: Int = 0): Message<V> {
        return MessageBuilder
            .withPayload(value!!)
            .setHeader(KEY, key?.toByteArray())
            .setHeader(PARTITION, partition)
            .build()
    }
}
