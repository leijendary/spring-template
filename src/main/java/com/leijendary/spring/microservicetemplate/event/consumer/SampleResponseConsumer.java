package com.leijendary.spring.microservicetemplate.event.consumer;

import com.leijendary.spring.microservicetemplate.data.response.SampleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SampleResponseConsumer {

    @Bean
    public Consumer<KStream<String, SampleResponse>> sampleResponseCreated1() {
        return stream -> stream.foreach((key, value) -> log.info("Created: '{}', '{}'", key, value));
    }

    @Bean
    public Consumer<KStream<String, SampleResponse>> sampleResponseUpdated1() {
        return stream -> stream.foreach((key, value) -> log.info("Updated: '{}', '{}'", key, value));
    }

    @Bean
    public Consumer<KStream<String, SampleResponse>> sampleResponseDeleted1() {
        return stream -> stream.foreach((key, value) -> log.info("Deleted: '{}', '{}'", key, value));
    }
}
