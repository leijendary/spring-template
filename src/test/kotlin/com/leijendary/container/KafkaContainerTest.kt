package com.leijendary.container

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

class KafkaContainerTest {
    companion object {
        private val image = DockerImageName.parse("confluentinc/cp-kafka:7.5.3")
        private val kafka = KafkaContainer(image).withKraft()
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
