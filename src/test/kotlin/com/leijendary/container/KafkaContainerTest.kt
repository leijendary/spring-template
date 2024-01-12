package com.leijendary.container

import org.springframework.aot.hint.ExecutableMode.INVOKE
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ImportRuntimeHints
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

@ImportRuntimeHints(KafkaContainerRuntimeHints::class)
class KafkaContainerTest {
    companion object {
        private val image = DockerImageName.parse("confluentinc/cp-kafka:7.5.3")
        private val kafka = KafkaContainer(image)
            .withEnv("KAFKA_HEAP_OPTS", "-Xms128m -Xmx256m")
            .withKraft()
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            kafka.start()

            val properties = arrayOf("spring.kafka.bootstrapServers=${kafka.bootstrapServers}")

            TestPropertyValues
                .of(*properties)
                .applyTo(applicationContext.environment)
        }
    }
}

class KafkaContainerRuntimeHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        hints.reflection().registerType(KafkaContainerTest.Initializer::class.java) {
            it.withConstructor(emptyList(), INVOKE)
        }
    }
}
