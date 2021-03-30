package com.leijendary.spring.microservicetemplate.event.producer;

import com.leijendary.spring.microservicetemplate.config.properties.InfoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY;
import static org.springframework.messaging.support.MessageBuilder.withPayload;

@Component
@RequiredArgsConstructor
public abstract class AppProducer<V> {

    private final InfoProperties infoProperties;
    private final StreamBridge streamBridge;

    public CompletableFuture<Boolean> keyPayload(final String topic, final String key, final V value) {
        final var destination = getPrefix() + "." + topic;
        final var message = withPayload(value)
                .setHeader(MESSAGE_KEY, key.getBytes(UTF_8))
                .build();

        return completedFuture(streamBridge.send(destination, message));
    }

    private String getPrefix() {
        return infoProperties.getApp().getArtifactId();
    }
}
