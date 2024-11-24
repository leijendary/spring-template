package com.leijendary

import com.github.dockerjava.api.command.InspectContainerResponse
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistrar
import org.testcontainers.containers.ContainerLaunchException
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.kafka.ConfluentKafkaContainer
import org.testcontainers.ollama.OllamaContainer

@TestConfiguration(proxyBeanMethods = false)
@Import(
    TestcontainersConfiguration.Elasticsearch::class,
    TestcontainersConfiguration.Kafka::class,
    TestcontainersConfiguration.Ollama::class,
    TestcontainersConfiguration.Postgres::class,
    TestcontainersConfiguration.Redis::class,
    TestcontainersConfiguration.Zipkin::class,
)
class TestcontainersConfiguration {
    @TestConfiguration(proxyBeanMethods = false)
    class Elasticsearch {
        @Bean
        @ServiceConnection
        fun elasticsearchContainer(): ElasticsearchContainer {
            return ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.17.10")
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    class Kafka {
        @Bean
        @ServiceConnection
        fun kafkaContainer(): ConfluentKafkaContainer {
            return ConfluentKafkaContainer("confluentinc/cp-kafka:7.7.0")
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    class Ollama {
        @Bean
        @ServiceConnection
        fun ollamaContainer() = object : OllamaContainer("ollama/ollama:0.3.11") {
            override fun containerIsStarted(containerInfo: InspectContainerResponse) {
                super.containerIsStarted(containerInfo)

                val result = execInContainer("ollama", "pull", "all-minilm")

                if (result.exitCode != 0 && result.stderr !== null) {
                    throw ContainerLaunchException(result.stderr)
                }
            }
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    class Postgres {
        @Bean
        @ServiceConnection
        fun postgresContainer(): PostgreSQLContainer<*> {
            return PostgreSQLContainer("pgvector/pgvector:pg16")
        }

        @Bean
        fun postgresProperties(postgresContainer: PostgreSQLContainer<*>) = DynamicPropertyRegistrar {
            it.add("spring.datasource.primary.jdbcUrl") { postgresContainer.jdbcUrl }
            it.add("spring.datasource.primary.username") { postgresContainer.username }
            it.add("spring.datasource.primary.password") { postgresContainer.password }
            it.add("spring.datasource.readonly.jdbcUrl") { postgresContainer.jdbcUrl }
            it.add("spring.datasource.readonly.username") { postgresContainer.username }
            it.add("spring.datasource.readonly.password") { postgresContainer.password }
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    class Redis {
        @Bean
        @ServiceConnection("redis")
        fun redisContainer(): GenericContainer<*> {
            return GenericContainer("redis:7-alpine").withExposedPorts(6379)
        }

        @Bean
        fun redisProperties(redisContainer: GenericContainer<*>) = DynamicPropertyRegistrar {
            it.add("spring.data.redis.port") { redisContainer.firstMappedPort }
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    class Zipkin {
        @Bean
        @ServiceConnection("openzipkin/zipkin")
        fun zipkinContainer(): GenericContainer<*> {
            return GenericContainer("openzipkin/zipkin-slim:3").withExposedPorts(9411)
        }
    }
}
