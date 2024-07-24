package com.leijendary.container

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class ZipkinContainerInitializer : GenericContainer<ZipkinContainerInitializer>(image) {
    override fun start() {
        withExposedPorts(9411)

        super.start()

        System.setProperty("management.zipkin.tracing.endpoint", "$host:$firstMappedPort/api/v2/spans")
    }

    companion object {
        val INSTANCE: ZipkinContainerInitializer by lazy { ZipkinContainerInitializer() }

        private val image = DockerImageName.parse("openzipkin/zipkin-slim:3")
    }
}
