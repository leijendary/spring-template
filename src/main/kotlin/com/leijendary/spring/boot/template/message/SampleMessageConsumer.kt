package com.leijendary.spring.boot.template.message

import com.leijendary.spring.boot.template.api.v1.data.SampleMessage
import com.leijendary.spring.boot.template.core.util.AnyUtil.toJson
import com.leijendary.spring.boot.template.core.util.logger
import org.apache.kafka.streams.kstream.KStream
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
class SampleMessageConsumer {
    val log = logger()

    @Bean
    fun sampleCreated(): Consumer<KStream<String, SampleMessage>> {
        return Consumer<KStream<String, SampleMessage>> { stream: KStream<String, SampleMessage> ->
            stream.foreach { key: String?, value: SampleMessage ->
                log.info("Created: '{}', '{}'", key, value.toJson())
            }
        }
    }

    @Bean
    fun sampleUpdated(): Consumer<KStream<String?, SampleMessage>> {
        return Consumer<KStream<String?, SampleMessage>> { stream: KStream<String?, SampleMessage> ->
            stream.foreach { key: String?, value: SampleMessage ->
                log.info("Updated: '{}', '{}'", key, value.toJson())
            }
        }
    }

    @Bean
    fun sampleDeleted(): Consumer<KStream<String?, SampleMessage>> {
        return Consumer<KStream<String?, SampleMessage>> { stream: KStream<String?, SampleMessage> ->
            stream.foreach { key: String?, value: SampleMessage ->
                log.info("Deleted: '{}', '{}'", key, value.toJson())
            }
        }
    }
}