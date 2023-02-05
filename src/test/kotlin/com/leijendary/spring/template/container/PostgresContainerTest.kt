package com.leijendary.spring.template.container

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresContainerTest {
    companion object {
        private val image = DockerImageName.parse("postgres:14-alpine")
        private val postgres = PostgreSQLContainer(image)
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            postgres.start()

            val properties = arrayOf(
                "spring.datasource.primary.jdbcUrl=${postgres.jdbcUrl}",
                "spring.datasource.primary.username=${postgres.username}",
                "spring.datasource.primary.password=${postgres.password}",
                "spring.datasource.readonly.jdbcUrl=${postgres.jdbcUrl}",
                "spring.datasource.readonly.username=${postgres.username}",
                "spring.datasource.readonly.password=${postgres.password}",
            )

            TestPropertyValues
                .of(*properties)
                .applyTo(applicationContext.environment)
        }
    }
}
