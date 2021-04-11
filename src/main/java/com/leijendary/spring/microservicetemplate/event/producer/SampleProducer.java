package com.leijendary.spring.microservicetemplate.event.producer;

import com.leijendary.spring.microservicetemplate.event.schema.SampleSchema;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.leijendary.spring.microservicetemplate.event.binding.SampleBinding.*;

@Component
public class SampleProducer extends AppProducer<SampleSchema> {

    public SampleProducer(final StreamBridge streamBridge) {
        super(streamBridge);
    }

    @Async
    public void created(SampleSchema sampleSchema) {
        keyPayload(CREATED, String.valueOf(sampleSchema.getId()), sampleSchema);
    }

    @Async
    public void updated(SampleSchema sampleSchema) {
        keyPayload(UPDATED, String.valueOf(sampleSchema.getId()), sampleSchema);
    }

    @Async
    public void deleted(SampleSchema sampleSchema) {
        keyPayload(DELETED, String.valueOf(sampleSchema.getId()), sampleSchema);
    }
}
