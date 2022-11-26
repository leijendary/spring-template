package com.leijendary.spring.template.core.message

import org.springframework.kafka.support.KafkaHeaders.KEY
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder

abstract class MessageProducer<V> {
    fun message(value: V): Message<V> {
        return message(null, value)
    }

    fun message(key: String?, value: V): Message<V> {
        var builder = MessageBuilder.withPayload(value!!)

        key?.let {
            builder = builder.setHeader(KEY, it.toByteArray())
        }

        return builder.build()
    }
}