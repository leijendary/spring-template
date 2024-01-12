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

@ImportRuntimeHints(RedisContainerRuntimeHints::class)
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

class RedisContainerRuntimeHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        hints.reflection().registerType(RedisContainerTest.Initializer::class.java) {
            it.withConstructor(emptyList(), INVOKE)
        }
    }
}
