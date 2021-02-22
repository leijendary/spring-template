package com.leijendary.spring.microservicetemplate.event.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public abstract class AppProducer<K, V> {

    private final KafkaTemplate<K, V> template;

    public CompletableFuture<SendResult<K, V>> keyPayload(String topic, K key, V value) {
        return template
                .send(topic, key, value)
                .completable();
    }
}
