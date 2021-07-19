package com.leijendary.spring.microservicetemplate.event.producer;

import com.leijendary.spring.microservicetemplate.event.schema.SampleSchema;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import static com.leijendary.spring.microservicetemplate.event.binding.SampleBinding.*;

@Component
public class SampleProducer extends AbstractProducer<SampleSchema> {

    public SampleProducer(final StreamBridge streamBridge) {
        super(streamBridge);
    }

    public void created(final SampleSchema sampleSchema) {
        send(CREATED, String.valueOf(sampleSchema.getId()), sampleSchema);
    }

    public void updated(final SampleSchema sampleSchema) {
        send(UPDATED, String.valueOf(sampleSchema.getId()), sampleSchema);
    }

    public void deleted(final SampleSchema sampleSchema) {
        send(DELETED, String.valueOf(sampleSchema.getId()), sampleSchema);
    }
}
