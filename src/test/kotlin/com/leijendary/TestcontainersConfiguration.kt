package com.leijendary

import com.github.dockerjava.api.command.InspectContainerResponse
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.ContainerLaunchException
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.ollama.OllamaContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {
    @Bean
    @ServiceConnection
    fun elasticsearchContainer(): ElasticsearchContainer {
        return ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.17.10")
    }

    @Bean
    @ServiceConnection
    fun kafkaContainer(): KafkaContainer {
        return KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.7.0")).withKraft()
    }

    @Bean
    @ServiceConnection
    fun ollamaContainer() = object : OllamaContainer("ollama/ollama:0.3.11") {
        override fun containerIsStarted(containerInfo: InspectContainerResponse?) {
            super.containerIsStarted(containerInfo)

            val result = execInContainer("ollama", "pull", "all-minilm")

            if (result.exitCode != 0 && result.stderr !== null) {
                throw ContainerLaunchException(result.stderr)
            }
        }
    }

    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> {
        return PostgreSQLContainer("pgvector/pgvector:pg16")
    }

    @Bean
    @ServiceConnection("redis")
    fun redisContainer(): GenericContainer<*> {
        return GenericContainer("redis:7-alpine").withExposedPorts(6379)
    }

    @Bean
    @ServiceConnection("openzipkin/zipkin")
    fun zipkinContainer(): GenericContainer<*> {
        return GenericContainer("openzipkin/zipkin-slim:3").withExposedPorts(9411)
    }
}
