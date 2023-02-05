package com.leijendary.spring.template.container

import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

val kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.1"))
