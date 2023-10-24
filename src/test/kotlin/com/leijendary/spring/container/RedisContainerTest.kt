package com.leijendary.spring.container

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class RedisContainerTest {
    companion object {
        private val image = DockerImageName.parse("redis:6-alpine")
        private val redis = GenericContainer(image).withExposedPorts(6379)
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            redis.start()

            val properties = arrayOf(
                "spring.data.redis.host=${redis.host}",
                "spring.data.redis.port=${redis.firstMappedPort}",
            )

            TestPropertyValues
                .of(*properties)
                .applyTo(applicationContext.environment)
        }
    }
}
