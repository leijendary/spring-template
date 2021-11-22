package com.leijendary.spring.boot.template.event.producer;

import com.leijendary.spring.boot.template.api.v1.data.SampleEvent;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class SampleProducer extends AppProducer<SampleEvent> {

    public SampleProducer(final StreamBridge streamBridge) {
        super(streamBridge);
    }

    public void create(final SampleEvent sampleEvent) {
        final var message = message(sampleEvent);

        streamBridge.send("sampleCreate-out-0", message);
    }

    public void update(final SampleEvent sampleEvent) {
        final var message = message(sampleEvent);

        streamBridge.send("sampleUpdate-out-0", message);
    }

    public void delete(final SampleEvent sampleEvent) {
        final var message = message(sampleEvent);

        streamBridge.send("sampleDelete-out-0", message);
    }
}
