package com.leijendary.spring

import com.leijendary.spring.container.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(
    initializers = [
        ElasticsearchContainerTest.Initializer::class,
        JaegerContainerTest.Initializer::class,
        KafkaContainerTest.Initializer::class,
        PostgresContainerTest.Initializer::class,
        RedisContainerTest.Initializer::class,
    ]
)
@AutoConfigureMockMvc
class ApplicationTests {
    @Test
    fun contextLoads() {
    }
}
