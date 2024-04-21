package com.leijendary.container

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class RedisContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
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

    companion object {
        private val image = DockerImageName.parse("redis:6-alpine")
        private val redis = GenericContainer(image).withExposedPorts(6379)
    }
}
