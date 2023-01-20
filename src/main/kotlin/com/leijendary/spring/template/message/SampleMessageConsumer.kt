package com.leijendary.spring.template.message

import com.leijendary.spring.template.api.v1.data.SampleMessage
import org.apache.kafka.streams.kstream.KStream
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class SampleMessageConsumer {
    @Bean
    fun sampleCreated(): Consumer<KStream<String, SampleMessage>> {
        return Consumer { }
    }

    @Bean
    fun sampleUpdated(): Consumer<KStream<String, SampleMessage>> {
        return Consumer { }
    }

    @Bean
    fun sampleDeleted(): Consumer<KStream<String, SampleMessage>> {
        return Consumer { }
    }
}
