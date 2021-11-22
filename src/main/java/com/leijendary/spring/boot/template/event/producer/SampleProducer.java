package com.leijendary.spring.boot.template.event.producer;

import com.leijendary.spring.boot.template.api.v1.data.SampleEvent;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SampleProducer {

    private static final String BINDING_CREATE = "sampleCreate-out-0";
    private static final String BINDING_UPDATE = "sampleUpdate-out-0";
    private static final String BINDING_DELETE = "sampleDelete-out-0";

    private final Producer<SampleEvent> producer;

    public void create(final SampleEvent sampleEvent) {
        producer.send(BINDING_CREATE, sampleEvent);
    }

    public void update(final SampleEvent sampleEvent) {
        producer.send(BINDING_UPDATE, sampleEvent);
    }

    public void delete(final SampleEvent sampleEvent) {
        producer.send(BINDING_DELETE, sampleEvent);
    }
}
