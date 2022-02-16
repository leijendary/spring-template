package com.leijendary.spring.boot.template.core.message

import com.leijendary.spring.boot.template.core.util.AnyUtil.toJson
import com.leijendary.spring.boot.template.core.util.logger
import org.springframework.cloud.stream.config.BindingServiceProperties
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.kafka.KafkaException
import org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder.withPayload
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class KafkaProducer<V : Any>(
    private val bindingServiceProperties: BindingServiceProperties,
    private val streamBridge: StreamBridge
) {
    val log = logger()

    @Retryable(
        value = [KafkaException::class],
        maxAttemptsExpression = "\${retry.maxAttempts}",
        backoff = Backoff(
            delayExpression = "\${retry.backoff.delay}",
            maxDelayExpression = "\${retry.backoff.maxDelay}",
            multiplierExpression = "\${retry.backoff.multiplier}"
        )
    )
    fun send(binding: String, value: V) {
        send(binding, null, value)
    }

    @Retryable(
        value = [KafkaException::class],
        maxAttemptsExpression = "\${retry.maxAttempts}",
        backoff = Backoff(
            delayExpression = "\${retry.backoff.delay}",
            maxDelayExpression = "\${retry.backoff.maxDelay}",
            multiplierExpression = "\${retry.backoff.multiplier}"
        )
    )
    fun send(binding: String, key: String?, value: V) {
        val topic: String = bindingServiceProperties.getBindingDestination(binding)
        val message = message(key, value)
        val result = streamBridge.send(binding, message)

        if (!result) {
            val errorMessage = "Message not sent to topic: $topic Key: $key with Message: ${value.toJson()}"

            log.warn(errorMessage)

            throw KafkaException(errorMessage)
        }

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