package com.leijendary.spring.template.message

import com.leijendary.spring.template.api.v1.data.SampleMessage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
class SampleMessageConsumer {
    @Bean
    fun sampleCreated(): Consumer<SampleMessage> {
        return Consumer { }
    }

    @Bean
    fun sampleUpdated(): Consumer<SampleMessage> {
        return Consumer { }
    }

    @Bean
    fun sampleDeleted(): Consumer<SampleMessage> {
        return Consumer { }
    }
}