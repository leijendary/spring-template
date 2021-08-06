package com.leijendary.spring.microservicetemplate.event.consumer;

import com.leijendary.schema.SampleSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SampleConsumer {

    @Bean
    public Consumer<KStream<String, SampleSchema>> sampleCreated() {
        return stream -> stream.foreach((key, value) -> log.info("Created: '{}', '{}'", key, value));
    }

    @Bean
    public Consumer<KStream<String, SampleSchema>> sampleUpdated() {
        return stream -> stream.foreach((key, value) -> log.info("Updated: '{}', '{}'", key, value));
    }

    @Bean
    public Consumer<KStream<String, SampleSchema>> sampleDeleted() {
        return stream -> stream.foreach((key, value) -> log.info("Deleted: '{}', '{}'", key, value));
    }
}
