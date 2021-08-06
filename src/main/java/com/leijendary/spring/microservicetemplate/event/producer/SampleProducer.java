package com.leijendary.spring.microservicetemplate.event.producer;

import com.leijendary.schema.SampleSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

import static reactor.core.publisher.Sinks.many;

@Component
public class SampleProducer extends AbstractProducer<SampleSchema> {

    private final Sinks.Many<Message<SampleSchema>> createBuffer = many().multicast().onBackpressureBuffer();
    private final Sinks.Many<Message<SampleSchema>> updateBuffer = many().multicast().onBackpressureBuffer();
    private final Sinks.Many<Message<SampleSchema>> deleteBuffer = many().multicast().onBackpressureBuffer();

    @Bean
    public Supplier<Flux<Message<SampleSchema>>> sampleCreate() {
        return createBuffer::asFlux;
    }

    @Bean
    public Supplier<Flux<Message<SampleSchema>>> sampleUpdate() {
        return updateBuffer::asFlux;
    }

    @Bean
    public Supplier<Flux<Message<SampleSchema>>> sampleDelete() {
        return deleteBuffer::asFlux;
    }

    public void create(final SampleSchema sampleSchema) {
        final var message = messageWithKey(String.valueOf(sampleSchema.getId()), sampleSchema);

        createBuffer.tryEmitNext(message);
    }

    public void update(final SampleSchema sampleSchema) {
        final var message = messageWithKey(String.valueOf(sampleSchema.getId()), sampleSchema);

        updateBuffer.tryEmitNext(message);
    }

    public void delete(final SampleSchema sampleSchema) {
        final var message = messageWithKey(String.valueOf(sampleSchema.getId()), sampleSchema);

        deleteBuffer.tryEmitNext(message);
    }
}
