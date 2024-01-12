package com.leijendary.container

import org.springframework.aot.hint.ExecutableMode.INVOKE
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ImportRuntimeHints
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

@ImportRuntimeHints(JaegerContainerRuntimeHints::class)
class JaegerContainerTest {
    companion object {
        private val image = DockerImageName.parse("jaegertracing/all-in-one:1")
        private val jaeger = GenericContainer(image)
            .withEnv("COLLECTOR_OTLP_ENABLED", "true")
            .withExposedPorts(4318)
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
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
    }
}

class JaegerContainerRuntimeHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        hints.reflection().registerType(JaegerContainerTest.Initializer::class.java) {
            it.withConstructor(emptyList(), INVOKE)
        }
    }
}
