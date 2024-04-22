package com.leijendary.integration

import com.leijendary.container.*
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertTrue

@Testcontainers
class SampleTestCase {
    @Test
    fun tada() {
        assertTrue(ELASTICSEARCH.isRunning)
        assertTrue(JAEGER.isRunning)
        assertTrue(DATABASE.isRunning)
        assertTrue(KAFKA.isRunning)
        assertTrue(REDIS.isRunning)
    }

    companion object {
        @Container
        val ELASTICSEARCH = ElasticsearchContainerInitializer.INSTANCE

        @Container
        val JAEGER = JaegerContainerInitializer.INSTANCE

        @Container
        val DATABASE = PostgresContainerInitializer.INSTANCE

        @Container
        val KAFKA = KafkaContainerInitializer.INSTANCE

        @Container
        val REDIS = RedisContainerInitializer.INSTANCE
    }
}
