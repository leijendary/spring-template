package com.leijendary.spring.template.container

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class JaegerContainerTest {
    companion object {
        private val image = DockerImageName.parse("jaegertracing/jaeger-collector:1")
        private val jaeger = GenericContainer(image).apply {
            addEnv("COLLECTOR_ZIPKIN_HTTP_PORT", "9411")
            withExposedPorts(9411)
        }
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            jaeger.start()

            val endpoint = "${jaeger.host}:${jaeger.firstMappedPort}/api/v2/spans"
            val properties = arrayOf("management.zipkin.tracing.endpoint=$endpoint")

            TestPropertyValues
                .of(*properties)
                .applyTo(applicationContext.environment)
        }
    }
}
