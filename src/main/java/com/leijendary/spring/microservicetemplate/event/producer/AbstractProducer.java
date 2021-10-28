package com.leijendary.spring.microservicetemplate.event.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

import static org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY;
import static org.springframework.messaging.support.MessageBuilder.withPayload;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractProducer<V> {

    private final Sinks.EmitFailureHandler failureHandler = (signalType, emitResult) -> {
        final var isFailure = emitResult.isFailure();

        if (isFailure) {
            log.warn("Sink emission failure signal type {} and result {}. Retrying...", signalType, emitResult);
        }

        return isFailure;
    };

    public Message<V> messageWithKey(final String key, final V value) {
        log.info("Sending Key: {} with Message: {}", key, value);

        return withPayload(value)
                .setHeader(MESSAGE_KEY, key)
                .build();
    }

    public Message<V> message(final V value) {
        log.info("Sending Message: {}", value);

        return withPayload(value).build();
    }

    public void emitNext(final Sinks.Many<Message<V>> buffer, final Message<V> message) {
        buffer.emitNext(message, failureHandler);
    }
}
