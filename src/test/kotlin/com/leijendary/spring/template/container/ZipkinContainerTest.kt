package com.leijendary.spring.template.container

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class ZipkinContainerTest {
    companion object {
        private val image = DockerImageName.parse("openzipkin/zipkin-slim:2")
        private val zipkin = GenericContainer(image).apply {
            withExposedPorts(9411)
        }
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            zipkin.start()

            val endpoint = "${zipkin.host}:${zipkin.firstMappedPort}/api/v2/spans"
            val properties = arrayOf("management.zipkin.tracing.endpoint=$endpoint")

            TestPropertyValues
                .of(*properties)
                .applyTo(applicationContext.environment)
        }
    }
}
