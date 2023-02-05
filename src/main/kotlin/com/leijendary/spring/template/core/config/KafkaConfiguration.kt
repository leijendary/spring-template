package com.leijendary.spring.template.core.config

import com.leijendary.spring.template.core.interceptor.KafkaInterceptor
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler

@Configuration
@EnableKafka
class KafkaConfiguration(private val kafkaProperties: KafkaProperties) {
    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        val config = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG to kafkaProperties.consumer.groupId,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
            ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG to KafkaInterceptor::class.java.name,
        )

        return DefaultKafkaConsumerFactory(config)
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        val config = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
            ProducerConfig.INTERCEPTOR_CLASSES_CONFIG to KafkaInterceptor::class.java.name,
        )

        return DefaultKafkaProducerFactory(config)
    }

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, String>,
        template: KafkaTemplate<String, String>
    ): ConcurrentKafkaListenerContainerFactory<String, String> {
        val recover = DeadLetterPublishingRecoverer(template) { record, _ ->
            TopicPartition(record.topic() + ".error", record.partition())
        }
        val errorHandler = DefaultErrorHandler(recover)
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory
        factory.containerProperties.isObservationEnabled = true
        factory.setCommonErrorHandler(errorHandler)

        return factory
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, String>): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory).apply {
            setMicrometerEnabled(true)
            setObservationEnabled(true)
            transactionIdPrefix
        }
    }
}
