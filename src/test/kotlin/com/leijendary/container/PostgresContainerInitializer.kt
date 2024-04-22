package com.leijendary.container

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresContainerInitializer : PostgreSQLContainer<PostgresContainerInitializer>(image) {
    override fun start() {
        super.start()

        System.setProperty("spring.datasource.primary.url", jdbcUrl)
        System.setProperty("spring.datasource.readOnly.url", jdbcUrl)
        System.setProperty("spring.datasource.username", username)
        System.setProperty("spring.datasource.password", password)
    }

    companion object {
        val INSTANCE: PostgresContainerInitializer by lazy { PostgresContainerInitializer() }

        private val image = DockerImageName.parse("postgres:15-alpine")
    }
}
