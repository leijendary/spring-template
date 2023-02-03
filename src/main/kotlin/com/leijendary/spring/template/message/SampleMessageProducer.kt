package com.leijendary.spring.template.message

import com.leijendary.spring.template.api.v1.model.SampleMessage
import com.leijendary.spring.template.core.extension.emit
import com.leijendary.spring.template.core.util.Messaging
import org.springframework.context.annotation.Bean
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import reactor.core.publisher.Sinks.many
import java.util.function.Supplier

@Component
class SampleMessageProducer {
    private val createBuffer = many().multicast().onBackpressureBuffer<Message<SampleMessage>>()
    private val updateBuffer = many().multicast().onBackpressureBuffer<Message<SampleMessage>>()
    private val deleteBuffer = many().multicast().onBackpressureBuffer<Message<SampleMessage>>()

    @Bean
    fun sampleCreate() = Supplier { createBuffer.asFlux() }

    @Bean
    fun sampleUpdate() = Supplier { updateBuffer.asFlux() }

    @Bean
    fun sampleDelete() = Supplier { deleteBuffer.asFlux() }

    fun create(sampleMessage: SampleMessage) {
        val message = Messaging.create(sampleMessage)

        createBuffer.emit(message)
    }

    fun update(sampleMessage: SampleMessage) {
        val message = Messaging.create(sampleMessage)

        updateBuffer.emit(message)
    }

    fun delete(sampleMessage: SampleMessage) {
        val message = Messaging.create(sampleMessage)

        deleteBuffer.emit(message)
    }
}
