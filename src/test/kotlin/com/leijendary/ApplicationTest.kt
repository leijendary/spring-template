package com.leijendary

import com.leijendary.container.*
import com.leijendary.model.IdentityModel
import org.junit.jupiter.api.Test
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ImportRuntimeHints
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
@ImportRuntimeHints(ApplicationTestRuntimeHints::class)
@AutoConfigureMockMvc
class ApplicationTest {
    @Test
    fun contextLoads() {
    }
}


class ApplicationTestRuntimeHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        // Resources
        hints.resources()
            .registerPattern("elasticsearch-default-memory-vm.options")

        // Serialization
        hints.serialization().registerType(IdentityModel::class.java)
    }
}
