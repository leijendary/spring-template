package com.leijendary.config

import com.leijendary.config.properties.DataSourcePrimaryProperties
import com.leijendary.config.properties.DataSourceReadOnlyProperties
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class DatabaseConfiguration(
    private val dataSourceProperties: DataSourceProperties,
    private val primaryProperties: DataSourcePrimaryProperties,
    private val readOnlyProperties: DataSourceReadOnlyProperties
) {
    @Bean
    @Primary
    fun dataSource(primaryDataSource: DataSource, readOnlyDataSource: DataSource): DataSource {
        return LazyConnectionDataSourceProxy(primaryDataSource).apply {
            setReadOnlyDataSource(readOnlyDataSource)
        }
    }

    @Bean
    fun primaryDataSource(): DataSource {
        val dataSource = HikariDataSource().apply {
            poolName = primaryProperties.name
            jdbcUrl = primaryProperties.url
            maximumPoolSize = primaryProperties.poolSize
        }
        dataSource.username = dataSourceProperties.username
        dataSource.password = dataSourceProperties.password

        return dataSource
    }

    @Bean
    fun readOnlyDataSource(): DataSource {
        val dataSource = HikariDataSource().apply {
            poolName = readOnlyProperties.name
            jdbcUrl = readOnlyProperties.url
            maximumPoolSize = readOnlyProperties.poolSize
        }
        dataSource.username = dataSourceProperties.username
        dataSource.password = dataSourceProperties.password

        return dataSource
    }
}
