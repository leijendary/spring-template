package com.leijendary

import com.leijendary.container.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
// Example of using test containers. Each test should only initialize the needed container.
@ContextConfiguration(
    initializers = [
        ElasticsearchContainerInitializer::class,
        JaegerContainerInitializer::class,
        KafkaContainerInitializer::class,
        PostgresContainerInitializer::class,
        RedisContainerInitializer::class,
    ]
)
class ApplicationTestCase {
    @Test
    fun contextLoads() {
    }
}
