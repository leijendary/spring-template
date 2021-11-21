package com.leijendary.spring.boot.template.event.producer;

import static reactor.core.publisher.Sinks.many;

import java.util.function.Supplier;

import com.leijendary.spring.boot.template.api.v1.data.SampleEvent;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class SampleProducer extends AppProducer<SampleEvent> {

    private final Sinks.Many<Message<SampleEvent>> createBuffer = many().multicast().onBackpressureBuffer();
    private final Sinks.Many<Message<SampleEvent>> updateBuffer = many().multicast().onBackpressureBuffer();
    private final Sinks.Many<Message<SampleEvent>> deleteBuffer = many().multicast().onBackpressureBuffer();

    @Bean
    public Supplier<Flux<Message<SampleEvent>>> sampleCreate() {
        return createBuffer::asFlux;
    }

    @Bean
    public Supplier<Flux<Message<SampleEvent>>> sampleUpdate() {
        return updateBuffer::asFlux;
    }

    @Bean
    public Supplier<Flux<Message<SampleEvent>>> sampleDelete() {
        return deleteBuffer::asFlux;
    }

    public void create(final SampleEvent sampleEvent) {
        final var message = message(sampleEvent);

        createBuffer.emitNext(message, failureHandler());
    }

    public void update(final SampleEvent sampleEvent) {
        final var message = message(sampleEvent);

        updateBuffer.emitNext(message, failureHandler());
    }

    public void delete(final SampleEvent sampleEvent) {
        final var message = message(sampleEvent);

        deleteBuffer.emitNext(message, failureHandler());
    }
}
