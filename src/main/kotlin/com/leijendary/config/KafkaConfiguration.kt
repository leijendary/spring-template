package com.leijendary.config

import com.leijendary.config.properties.KafkaTopicProperties
import org.apache.kafka.common.TopicPartition
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaAdmin.NewTopics
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler

private const val TOPIC_DEAD_LETTER_SUFFIX = ".error"

object Topic {
    const val SAMPLE_CREATED = "sampleCreated"
    const val SAMPLE_UPDATED = "sampleUpdated"
    const val SAMPLE_DELETED = "sampleDeleted"
}

@Configuration
@EnableKafka
class KafkaConfiguration(
    private val kafkaProperties: KafkaProperties,
    private val kafkaTopicProperties: KafkaTopicProperties
) {
    @Bean
    fun topics(): NewTopics {
        val topics = kafkaTopicProperties.values.flatMap {
            val topic = TopicBuilder
                .name(it.name)
                .partitions(it.partitions)
                .replicas(it.replicas)
                .build()
            val deadLetterTopic = TopicBuilder
                .name("${it.name}$TOPIC_DEAD_LETTER_SUFFIX")
                .partitions(1)
                .replicas(1)
                .build()

            listOf(topic, deadLetterTopic)
        }

        return NewTopics(*topics.toTypedArray())
    }

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, String>,
        template: KafkaTemplate<String, String>
    ): ConcurrentKafkaListenerContainerFactory<String, String> {
        val recover = DeadLetterPublishingRecoverer(template) { record, _ ->
            TopicPartition(record.topic() + TOPIC_DEAD_LETTER_SUFFIX, record.partition())
        }
        val errorHandler = DefaultErrorHandler(recover)

        return ConcurrentKafkaListenerContainerFactory<String, String>().apply {
            this.consumerFactory = consumerFactory
            containerProperties.isMicrometerEnabled = true
            containerProperties.isObservationEnabled = true
            setCommonErrorHandler(errorHandler)
            setConcurrency(kafkaProperties.listener.concurrency)
        }
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, String>): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory).apply {
            setMicrometerEnabled(true)
            setObservationEnabled(true)
        }
    }
}
