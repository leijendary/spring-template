package com.leijendary

import com.leijendary.container.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertTrue

@SpringBootTest
@Testcontainers
class ApplicationTestCase {
    @Test
    fun contextLoads() {
    }

    @Test
    fun `containers are running`() {
        assertTrue(ELASTICSEARCH.isRunning)
        assertTrue(JAEGER.isRunning)
        assertTrue(DATABASE.isRunning)
        assertTrue(KAFKA.isRunning)
        assertTrue(REDIS.isRunning)
    }

    companion object {
        @Container
        private val ELASTICSEARCH = ElasticsearchContainerInitializer.INSTANCE

        @Container
        private val JAEGER = JaegerContainerInitializer.INSTANCE

        @Container
        private val DATABASE = PostgresContainerInitializer.INSTANCE

        @Container
        private val KAFKA = KafkaContainerInitializer.INSTANCE

        @Container
        private val REDIS = RedisContainerInitializer.INSTANCE
    }
}
