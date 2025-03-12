package com.leijendary.config

import com.leijendary.config.properties.KafkaTopicProperties
import com.leijendary.interceptor.KafkaInterceptor
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor.AnnotationEnhancer
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(KafkaTopicProperties::class)
@EnableKafka
class KafkaConfiguration(
    private val kafkaInterceptor: KafkaInterceptor,
    private val kafkaProperties: KafkaProperties,
    private val kafkaTopicProperties: KafkaTopicProperties,
) {
    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, String>
    ): KafkaListenerContainerFactory<*> {
        return ConcurrentKafkaListenerContainerFactory<String, String>().apply {
            containerProperties.ackMode = kafkaProperties.listener.ackMode
            containerProperties.isObservationEnabled = kafkaProperties.listener.isObservationEnabled
            setConsumerFactory(consumerFactory)
            setRecordInterceptor(kafkaInterceptor)
        }
    }

    @Bean
    fun topicConcurrencyEnhancer() = AnnotationEnhancer { attributes, method ->
        val annotation = method.getAnnotation(KafkaListener::class.java)
        var concurrency = annotation.topics.maxOf { kafkaTopicProperties[it]?.partitions ?: 1 }
        attributes["concurrency"] = concurrency.toString()
        attributes
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, String>): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory).apply {
            setObservationEnabled(kafkaProperties.template.isObservationEnabled)
            setProducerInterceptor(kafkaInterceptor)
        }
    }
}
