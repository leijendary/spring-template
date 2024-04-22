package com.leijendary.container

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class JaegerContainerInitializer : GenericContainer<JaegerContainerInitializer>(image) {
    override fun start() {
        withEnv("COLLECTOR_OTLP_ENABLED", "true")
        withExposedPorts(4318)

        super.start()

        System.setProperty("management.otlp.metrics.export.url", "$host:$firstMappedPort/v1/metrics")
        System.setProperty("management.otlp.tracing.endpoint", "$host:$firstMappedPort/v1/traces")
    }

    companion object {
        val INSTANCE: JaegerContainerInitializer by lazy { JaegerContainerInitializer() }

        private val image = DockerImageName.parse("jaegertracing/all-in-one:1")
    }
}
