package com.leijendary

import com.leijendary.container.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(
    initializers = [
        ElasticsearchContainerInitializer::class,
        JaegerContainerInitializer::class,
        KafkaContainerInitializer::class,
        PostgresContainerInitializer::class,
        RedisContainerInitializer::class,
    ]
)
@AutoConfigureMockMvc
class ApplicationTestCase {
    @Test
    fun contextLoads() {
    }
}
