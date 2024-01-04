package com.leijendary.interceptor

import com.leijendary.extension.logger
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerInterceptor
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.springframework.kafka.listener.RecordInterceptor
import org.springframework.stereotype.Component

@Component
class KafkaInterceptor : ProducerInterceptor<String, String>, RecordInterceptor<String, String> {
    private val log = logger()

    override fun onSend(record: ProducerRecord<String, String>): ProducerRecord<String, String> {
        val topic = record.topic()
        val partition = record.partition()
        val key = record.key()
        val payload = record.value()

        log.info("Sent to topic '$topic' on partition '$partition' with key '$key' and payload '$payload'")

        return record
    }

    override fun configure(configs: MutableMap<String, *>) {
        // No configuration needed for this
    }

    override fun onAcknowledgement(metadata: RecordMetadata, exception: Exception?) {
        // No configuration needed for this
    }

    override fun close() {
        // No configuration needed for this
    }

    override fun intercept(
        record: ConsumerRecord<String, String>,
        consumer: Consumer<String, String>
    ): ConsumerRecord<String, String> {
        val topic = record.topic()
        val partition = record.partition()
        val key = record.key()
        val payload = record.value()

        log.info("Received from topic '$topic' on partition '$partition' with key '$key' and payload '$payload'")

        return record
    }
}
