package com.leijendary.spring.microservicetemplate.event.producer;

import com.leijendary.spring.microservicetemplate.event.schema.SampleSchema;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.leijendary.spring.microservicetemplate.event.binding.SampleBinding.*;

@Component
public class SampleProducer extends AppProducer<SampleSchema> {

    public SampleProducer(final StreamBridge streamBridge) {
        super(streamBridge);
    }

    @Async
    public CompletableFuture<Boolean> created(final SampleSchema sampleSchema) {
        return keyPayload(CREATED, String.valueOf(sampleSchema.getId()), sampleSchema);
    }

    @Async
    public CompletableFuture<Boolean> updated(final SampleSchema sampleSchema) {
        return keyPayload(UPDATED, String.valueOf(sampleSchema.getId()), sampleSchema);
    }

    @Async
    public CompletableFuture<Boolean> deleted(final SampleSchema sampleSchema) {
        return keyPayload(DELETED, String.valueOf(sampleSchema.getId()), sampleSchema);
    }
}
