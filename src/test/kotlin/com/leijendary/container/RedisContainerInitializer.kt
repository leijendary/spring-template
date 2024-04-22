package com.leijendary.container

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class RedisContainerInitializer : GenericContainer<RedisContainerInitializer>(image) {
    override fun start() {
        withExposedPorts(6379)

        super.start()

        System.setProperty("spring.data.redis.host", host)
        System.setProperty("spring.data.redis.port", firstMappedPort.toString())
    }

    companion object {
        val INSTANCE: RedisContainerInitializer by lazy { RedisContainerInitializer() }

        private val image = DockerImageName.parse("redis:6-alpine")
    }
}
