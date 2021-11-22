package com.leijendary.spring.boot.template.event.producer;

import static com.leijendary.spring.boot.template.util.JsonUtil.toJson;
import static org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY;
import static org.springframework.messaging.support.MessageBuilder.withPayload;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class AppProducer<V> {

    protected final StreamBridge streamBridge;

    public Message<V> messageWithKey(final String key, final V value) {
        log.info("Sending Key: {} with Message: {}", key, toJson(value));

        return withPayload(value)
                .setHeader(MESSAGE_KEY, key)
                .build();
    }

    public Message<V> message(final V value) {
        log.info("Sending Message: {}", toJson(value));

        return withPayload(value).build();
    }
}
