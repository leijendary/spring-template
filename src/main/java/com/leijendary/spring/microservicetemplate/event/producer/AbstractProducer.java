package com.leijendary.spring.microservicetemplate.event.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY;
import static org.springframework.messaging.support.MessageBuilder.withPayload;

@Component
@RequiredArgsConstructor
public abstract class AbstractProducer<V> {

    private final StreamBridge streamBridge;

    public CompletableFuture<Boolean> keyPayload(final String binding, final String key, final V value) {
        final var message = withPayload(value)
                .setHeader(MESSAGE_KEY, key)
                .build();

        return completedFuture(streamBridge.send(binding, message));
    }
}
