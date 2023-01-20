package com.leijendary.spring.template.core.interceptor

import com.leijendary.spring.template.core.extension.logger
import com.leijendary.spring.template.core.util.TRACE_PARENT_HEADER
import com.leijendary.spring.template.core.util.Tracing
import org.apache.kafka.clients.consumer.ConsumerInterceptor
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.clients.producer.ProducerInterceptor
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.header.Headers
import org.springframework.stereotype.Component

@Component
class KafkaLoggingInterceptor : ProducerInterceptor<String, Any>, ConsumerInterceptor<String, Any> {
    private val log = logger()

    override fun onSend(record: ProducerRecord<String, Any>): ProducerRecord<String, Any> {
        val topic = record.topic()
        val partition = record.partition()
        val key = record.key()
        val payload = String(record.value() as ByteArray)
        val traceParent = traceHeader(record.headers())

        Tracing.log(traceParent) {
            log.info("Sent to topic '$topic' on partition '$partition' with key '$key' and payload '$payload'")
        }

        return record
    }

    override fun onConsume(records: ConsumerRecords<String, Any>): ConsumerRecords<String, Any> {
        records.forEach {
            val topic = it.topic()
            val partition = it.partition()
            val key = it.key()
            val payload = String(it.value() as ByteArray)
            val traceParent = traceHeader(it.headers())
            val text = "Received from topic '$topic' on partition '$partition' with key '$key' and payload '$payload'"

            Tracing.log(traceParent) {
                log.info(text)
            }
        }

        return records
    }

    override fun configure(configs: MutableMap<String, *>) {}

    override fun onAcknowledgement(metadata: RecordMetadata, exception: Exception?) {}

    override fun onCommit(offsets: MutableMap<TopicPartition, OffsetAndMetadata>) {}

    override fun close() {}

    private fun traceHeader(headers: Headers) = headers.lastHeader(TRACE_PARENT_HEADER).value().let { String(it) }
}
