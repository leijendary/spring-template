package com.leijendary.spring.boot.template.event.producer;

import com.leijendary.spring.boot.core.event.producer.AppProducer;
import com.leijendary.spring.boot.template.data.SampleData;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

import static reactor.core.publisher.Sinks.many;

@Component
public class SampleProducer extends AppProducer<SampleData> {

    private final Sinks.Many<Message<SampleData>> createBuffer = many().multicast().onBackpressureBuffer();
    private final Sinks.Many<Message<SampleData>> updateBuffer = many().multicast().onBackpressureBuffer();
    private final Sinks.Many<Message<SampleData>> deleteBuffer = many().multicast().onBackpressureBuffer();

    @Bean
    public Supplier<Flux<Message<SampleData>>> sampleCreate() {
        return createBuffer::asFlux;
    }

    @Bean
    public Supplier<Flux<Message<SampleData>>> sampleUpdate() {
        return updateBuffer::asFlux;
    }

    @Bean
    public Supplier<Flux<Message<SampleData>>> sampleDelete() {
        return deleteBuffer::asFlux;
    }

    public void create(final SampleData sampleData) {
        final var message = message(sampleData);

        emitNext(createBuffer, message);
    }

    public void update(final SampleData sampleData) {
        final var message = message(sampleData);

        emitNext(updateBuffer, message);
    }

    public void delete(final SampleData sampleData) {
        final var message = message(sampleData);

        emitNext(deleteBuffer, message);
    }
}
