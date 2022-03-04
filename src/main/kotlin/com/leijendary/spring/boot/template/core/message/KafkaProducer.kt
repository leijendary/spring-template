package com.leijendary.spring.boot.template.core.message

import com.leijendary.spring.boot.template.core.extension.AnyUtil.toJson
import com.leijendary.spring.boot.template.core.extension.logger
import com.leijendary.spring.boot.template.core.util.RETRY_BACKOFF_DELAY
import com.leijendary.spring.boot.template.core.util.RETRY_BACKOFF_MAX_DELAY
import com.leijendary.spring.boot.template.core.util.RETRY_BACKOFF_MULTIPLIER
import com.leijendary.spring.boot.template.core.util.RETRY_MAX_ATTEMPTS
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
        maxAttemptsExpression = RETRY_MAX_ATTEMPTS,
        backoff = Backoff(
            delayExpression = RETRY_BACKOFF_DELAY,
            maxDelayExpression = RETRY_BACKOFF_MAX_DELAY,
            multiplierExpression = RETRY_BACKOFF_MULTIPLIER
        )
    )
    fun send(binding: String, value: V) {
        send(binding, null, value)
    }

    @Retryable(
        value = [KafkaException::class],
        maxAttemptsExpression = RETRY_MAX_ATTEMPTS,
        backoff = Backoff(
            delayExpression = RETRY_BACKOFF_DELAY,
            maxDelayExpression = RETRY_BACKOFF_MAX_DELAY,
            multiplierExpression = RETRY_BACKOFF_MULTIPLIER
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