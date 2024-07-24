package com.leijendary

import com.leijendary.container.ElasticsearchContainerInitializer
import com.leijendary.container.KafkaContainerInitializer
import com.leijendary.container.PostgresContainerInitializer
import com.leijendary.container.RedisContainerInitializer
import com.leijendary.container.ZipkinContainerInitializer
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
        assertTrue(DATABASE.isRunning)
        assertTrue(KAFKA.isRunning)
        assertTrue(REDIS.isRunning)
        assertTrue(ZIPKIN.isRunning)
    }

    companion object {
        @Container
        private val ELASTICSEARCH = ElasticsearchContainerInitializer.INSTANCE

        @Container
        private val DATABASE = PostgresContainerInitializer.INSTANCE

        @Container
        private val KAFKA = KafkaContainerInitializer.INSTANCE

        @Container
        private val REDIS = RedisContainerInitializer.INSTANCE

        @Container
        private val ZIPKIN = ZipkinContainerInitializer.INSTANCE
    }
}
