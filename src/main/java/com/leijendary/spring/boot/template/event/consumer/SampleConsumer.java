package com.leijendary.spring.boot.template.event.consumer;

import static com.leijendary.spring.boot.template.util.JsonUtil.toJson;

import java.util.function.Consumer;

import com.leijendary.spring.boot.template.api.v1.data.SampleEvent;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SampleConsumer {

    @Bean
    public Consumer<KStream<String, SampleEvent>> sampleCreated() {
        return stream -> stream.foreach((key, value) -> log.info("Created: '{}', '{}'", key, toJson(value)));
    }

    @Bean
    public Consumer<KStream<String, SampleEvent>> sampleUpdated() {
        return stream -> stream.foreach((key, value) -> log.info("Updated: '{}', '{}'", key, toJson(value)));
    }

    @Bean
    public Consumer<KStream<String, SampleEvent>> sampleDeleted() {
        return stream -> stream.foreach((key, value) -> log.info("Deleted: '{}', '{}'", key, toJson(value)));
    }
}
