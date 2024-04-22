package com.leijendary.container

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class JaegerContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        jaeger.start()

        val properties = arrayOf(
            "management.otlp.metrics.export.url=${jaeger.host}:${jaeger.firstMappedPort}/v1/metrics",
            "management.otlp.tracing.endpoint=${jaeger.host}:${jaeger.firstMappedPort}/v1/traces"
        )

        TestPropertyValues
            .of(*properties)
            .applyTo(applicationContext.environment)
    }

    companion object {
        private val image = DockerImageName.parse("jaegertracing/all-in-one:1")
        private val jaeger = GenericContainer(image)
            .withEnv("COLLECTOR_OTLP_ENABLED", "true")
            .withExposedPorts(4318)
            .withReuse(true)
    }
}
