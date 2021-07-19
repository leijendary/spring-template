package com.leijendary.spring.microservicetemplate.event.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY;
import static org.springframework.messaging.support.MessageBuilder.withPayload;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractProducer<V> {

    private final StreamBridge streamBridge;

    public void send(final String binding, final String key, final V value) {
        final var message = withPayload(value)
                .setHeader(MESSAGE_KEY, key)
                .build();

        send(binding, message);
    }

    @Retryable(value = MessageDeliveryException.class)
    public void send(final String binding, final Message<V> message) {
        final var key = message.getHeaders().getOrDefault(MESSAGE_KEY, "<empty>");
        final var errorMessage = String.format("Message key '%s' was not sent to binding '%s'", key, binding);
        var isSent = false;

        try {
            isSent = streamBridge.send(binding, message);
        } catch (Exception e) {
            log.error(errorMessage, e);

            throw new MessageDeliveryException(message, e);
        }

        if (!isSent) {
            log.error(errorMessage);

            throw new MessageDeliveryException(message, errorMessage);
        }
    }
}
