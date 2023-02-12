package com.leijendary.spring.template.core.config

import com.leijendary.spring.template.core.config.properties.DataSourcePrimaryProperties
import com.leijendary.spring.template.core.config.properties.DataSourceReadonlyProperties
import com.leijendary.spring.template.core.datasource.ReplicaAwareTransactionManager
import com.leijendary.spring.template.core.datasource.TransactionRoutingDataSource
import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource


@Configuration
class DatabaseConfiguration(
    private val primaryProperties: DataSourcePrimaryProperties,
    private val readonlyProperties: DataSourceReadonlyProperties
) {
    @Bean
    @Primary
    fun dataSource(routingDataSource: DataSource): DataSource {
        return LazyConnectionDataSourceProxy(routingDataSource)
    }

    @Bean
    fun routingDataSource(): DataSource {
        val primary = HikariDataSource(primaryProperties)
        val readonly = HikariDataSource(readonlyProperties)

        return TransactionRoutingDataSource(primary, readonly)
    }

    @Bean
    @Primary
    fun transactionManager(jpaTransactionManager: JpaTransactionManager): PlatformTransactionManager {
        return ReplicaAwareTransactionManager(jpaTransactionManager)
    }

    @Bean
    fun jpaTransactionManager(entityManagerFactory: EntityManagerFactory): JpaTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}
