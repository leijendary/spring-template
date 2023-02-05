package com.leijendary.spring.template.container

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName

class ElasticsearchContainerTest {
    companion object {
        private val image = DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.5.1")
        private val elasticsearch = ElasticsearchContainer(image).apply {
            addEnv("discovery.type", "single-node")
            addEnv("bootstrap.memory_lock", "true")
            addEnv("xpack.security.transport.ssl.enabled", "false")
            addEnv("ES_JAVA_OPTS", "-Xms256m -Xmx512m")
        }
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            elasticsearch.start()

            val properties = arrayOf(
                "spring.elasticsearch.uris=${elasticsearch.httpHostAddress}",
                "spring.elasticsearch.username=elastic",
                "spring.elasticsearch.password=changeme",
            )

            TestPropertyValues
                .of(*properties)
                .applyTo(applicationContext.environment)
        }
    }
}
