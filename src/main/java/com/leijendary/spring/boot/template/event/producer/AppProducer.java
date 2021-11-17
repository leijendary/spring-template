package com.leijendary.spring.boot.template.event.producer;

import static org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY;
import static org.springframework.messaging.support.MessageBuilder.withPayload;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class AppProducer<V> {

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

    public Sinks.EmitFailureHandler failureHandler() {
        return (signalType, emitResult) -> {
            final var isFailure = emitResult.isFailure();

            if (isFailure) {
                log.warn("Sink emission failure signal type {} and result {}. Retrying...", signalType, emitResult);
            }

            return isFailure;
        };
    }
}
