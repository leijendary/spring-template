package com.leijendary.spring.boot.template.event.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.leijendary.spring.boot.template.util.JsonUtil.toJson;
import static org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY;
import static org.springframework.messaging.support.MessageBuilder.withPayload;

@Component
@RequiredArgsConstructor
@Slf4j
public class Producer<V> {

    private final BindingServiceProperties bindingServiceProperties;
    private final StreamBridge streamBridge;

    public void send(final String binding, final V value) {
        send(binding, null, value);
    }

    public void send(final String binding, final String key, final V value) {
        final var topic = bindingServiceProperties.getBindingDestination(binding);
        final var message = message(key, value);

        streamBridge.send(binding, message);

        log.info("Sent to topic: {} Key: {} with Message: {}", topic, key, toJson(value));
    }

    private Message<V> message(final String key, final V value) {
        final var builder = withPayload(value);

        if (key != null) {
            builder.setHeader(MESSAGE_KEY, key);
        }

        return builder.build();
    }
}
