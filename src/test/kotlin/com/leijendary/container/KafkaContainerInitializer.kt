package com.leijendary.container

import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

class KafkaContainerInitializer : KafkaContainer(image) {
    override fun start() {
        withKraft()

        super.start()

        System.setProperty("spring.kafka.bootstrapServers", bootstrapServers)
    }

    companion object {
        val INSTANCE: KafkaContainerInitializer by lazy { KafkaContainerInitializer() }

        private val image = DockerImageName.parse("confluentinc/cp-kafka:7.5.3")
    }
}
