package com.leijendary.spring.microservicetemplate.event.producer;

import com.leijendary.spring.microservicetemplate.data.response.SampleResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.leijendary.spring.microservicetemplate.event.topic.SampleResponseTopic.*;

@Component
public class SampleResponseProducer extends AppProducer<String, SampleResponse> {

    public SampleResponseProducer(KafkaTemplate<String, SampleResponse> template) {
        super(template);
    }

    @Async
    public void created1(SampleResponse sampleResponse) {
        keyPayload(CREATED_V1, String.valueOf(sampleResponse.getId()), sampleResponse);
    }

    @Async
    public void updated1(SampleResponse sampleResponse) {
        keyPayload(UPDATED_V1, String.valueOf(sampleResponse.getId()), sampleResponse);
    }

    @Async
    public void deleted1(SampleResponse sampleResponse) {
        keyPayload(DELETED_V1, String.valueOf(sampleResponse.getId()), sampleResponse);
    }
}
