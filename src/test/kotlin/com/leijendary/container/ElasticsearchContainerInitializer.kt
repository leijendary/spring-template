package com.leijendary.container

import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName

class ElasticsearchContainerInitializer : ElasticsearchContainer(image) {
    override fun start() {
        withEnv("xpack.security.transport.ssl.enabled", "false")

        super.start()

        System.setProperty("spring.elasticsearch.uris", httpHostAddress)
        System.setProperty("spring.elasticsearch.username", "elastic")
        System.setProperty("spring.elasticsearch.password", ELASTICSEARCH_DEFAULT_PASSWORD)
    }

    companion object {
        val INSTANCE: ElasticsearchContainerInitializer by lazy { ElasticsearchContainerInitializer() }

        private val image = DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.8.2")
    }
}
