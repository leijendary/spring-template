package com.leijendary.container

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName

class ElasticsearchContainerTest {
    companion object {
        private val image = DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.8.2")
        private val elasticsearch = ElasticsearchContainer(image)
            .withEnv("discovery.type", "single-node")
            .withEnv("bootstrap.memory_lock", "true")
            .withEnv("xpack.security.transport.ssl.enabled", "false")
            .withEnv("ES_JAVA_OPTS", "-Xms128m -Xmx256m")
    }

    inner class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
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
