package com.leijendary.spring.core.interceptor

import com.leijendary.spring.core.extension.logger
import com.leijendary.spring.core.util.HEADER_TRACE_PARENT
import com.leijendary.spring.core.util.Tracing
import org.apache.kafka.clients.consumer.ConsumerInterceptor
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.clients.producer.ProducerInterceptor
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.TopicPartition

class KafkaInterceptor : ProducerInterceptor<String, Any>, ConsumerInterceptor<String, Any> {
    private val log = logger()

    override fun onSend(record: ProducerRecord<String, Any>): ProducerRecord<String, Any> {
        val topic = record.topic()
        val partition = record.partition()
        val key = record.key()
        val payload = record.value()

        log.info("Sent to topic '$topic' on partition '$partition' with key '$key' and payload '$payload'")

        return record
    }

    override fun onConsume(records: ConsumerRecords<String, Any>): ConsumerRecords<String, Any> {
        records.forEach {
            val topic = it.topic()
            val partition = it.partition()
            val key = it.key()
            val payload = it.value()
            val traceParent = it.headers()
                .lastHeader(HEADER_TRACE_PARENT)
                ?.value()
                ?.let(::String)
            val text = "Received from topic '$topic' on partition '$partition' with key '$key' and payload '$payload'"

            Tracing.log(traceParent) {
                log.info(text)
            }
        }

        return records
    }

    override fun configure(configs: MutableMap<String, *>) {
        // No configuration needed for this
    }

    override fun onAcknowledgement(metadata: RecordMetadata, exception: Exception?) {
        // No configuration needed for this
    }

    override fun onCommit(offsets: MutableMap<TopicPartition, OffsetAndMetadata>) {
        // No configuration needed for this
    }

    override fun close() {
        // No configuration needed for this
    }
}
