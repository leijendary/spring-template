package com.leijendary.spring.microservicetemplate.event.consumer;

import com.leijendary.spring.microservicetemplate.cache.SampleResponseCache;
import com.leijendary.spring.microservicetemplate.cache.SampleResponsePageCache;
import com.leijendary.spring.microservicetemplate.data.response.SampleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

import static java.lang.Integer.parseInt;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SampleResponseConsumer {

    private final SampleResponseCache sampleResponseCache;
    private final SampleResponsePageCache sampleResponsePageCache;

    @Bean
    public Consumer<KStream<String, SampleResponse>> sampleResponseCreated1() {
        return stream -> stream.foreach((key, value) -> {
            invalidatePageCache();
            setCache(parseInt(key), value);

            log.info("Created: '{}', '{}'", key, value);
        });
    }

    @Bean
    public Consumer<KStream<String, SampleResponse>> sampleResponseUpdated1() {
        return stream -> stream.foreach((key, value) -> {
            invalidatePageCache();
            setCache(parseInt(key), value);

            log.info("Updated: '{}', '{}'", key, value);
        });
    }

    @Bean
    public Consumer<KStream<String, SampleResponse>> sampleResponseDeleted1() {
        return stream -> stream.foreach((key, value) -> {
            invalidatePageCache();
            deleteCache(parseInt(key));

            log.info("Deleted: '{}', '{}'", key, value);
        });
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
