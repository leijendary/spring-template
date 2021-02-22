package com.leijendary.spring.microservicetemplate.event.consumer;

import com.leijendary.spring.microservicetemplate.cache.SampleResponseCache;
import com.leijendary.spring.microservicetemplate.cache.SampleResponsePageCache;
import com.leijendary.spring.microservicetemplate.data.response.SampleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;

import static com.leijendary.spring.microservicetemplate.event.topic.SampleResponseTopic.*;
import static java.lang.Integer.parseInt;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_MESSAGE_KEY;

@Configuration
@RequiredArgsConstructor
public class SampleResponseConsumer {

    private final SampleResponseCache sampleResponseCache;
    private final SampleResponsePageCache sampleResponsePageCache;

    @KafkaListener(topics = CREATED_V1)
    public void created1(@Header(name = RECEIVED_MESSAGE_KEY) String key, SampleResponse sampleResponse) {
        invalidatePageCache();
        setCache(parseInt(key), sampleResponse);

        System.out.println("CREATED : " + sampleResponse);
    }

    @KafkaListener(topics = UPDATED_V1)
    public void updated1(@Header(name = RECEIVED_MESSAGE_KEY) String key, SampleResponse sampleResponse) {
        invalidatePageCache();
        setCache(parseInt(key), sampleResponse);

        System.out.println("UPDATED : " + sampleResponse);
    }

    @KafkaListener(topics = DELETED_V1)
    public void deleted1(@Header(name = RECEIVED_MESSAGE_KEY) String key, SampleResponse sampleResponse) {
        invalidatePageCache();
        deleteCache(parseInt(key));

        System.out.println("DELETED : " + sampleResponse);
    }

    private void invalidatePageCache() {
        sampleResponsePageCache.clear();
    }

    private void setCache(int id, SampleResponse sampleResponse) {
        sampleResponseCache.put(id, sampleResponse);
    }

    private void deleteCache(int id) {
        sampleResponseCache.evict(id);
    }
}
