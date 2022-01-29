package com.leijendary.spring.boot.template.core.event

import com.leijendary.spring.boot.template.core.util.AnyUtil.toJson
import com.leijendary.spring.boot.template.core.util.logger
import org.springframework.cloud.stream.config.BindingServiceProperties
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder.withPayload
import org.springframework.stereotype.Component

@Component
class KafkaProducer<V : Any>(
    private val bindingServiceProperties: BindingServiceProperties,
    private val streamBridge: StreamBridge
) {
    val log = logger();

    fun send(binding: String, value: V) {
        send(binding, null, value)
    }

    fun send(binding: String, key: String?, value: V) {
        val topic: String = bindingServiceProperties.getBindingDestination(binding)
        val message = message(key, value)

        streamBridge.send(binding, message)

        log.info("Sent to topic: {} Key: {} with Message: {}", topic, key, value.toJson())
    }

    private fun message(key: String?, value: V): Message<V> {
        val builder = withPayload(value)

        if (key != null) {
            builder.setHeader(MESSAGE_KEY, key)
        }

        return builder.build()
    }
}