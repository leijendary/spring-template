package com.leijendary.container

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresContainerTest {
    companion object {
        private val image = DockerImageName.parse("postgres:15-alpine")
        private val postgres = PostgreSQLContainer(image)
    }

    inner class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            postgres.start()

            val properties = arrayOf(
                "spring.datasource.primary.jdbcUrl=${postgres.jdbcUrl}",
                "spring.datasource.readOnly.jdbcUrl=${postgres.jdbcUrl}",
                "spring.datasource.username=${postgres.username}",
                "spring.datasource.password=${postgres.password}",
            )

            TestPropertyValues
                .of(*properties)
                .applyTo(applicationContext.environment)
        }
    }
}
