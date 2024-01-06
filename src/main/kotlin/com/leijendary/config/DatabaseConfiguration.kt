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
        primaryProperties.username = dataSourceProperties.username
        primaryProperties.password = dataSourceProperties.password

        return HikariDataSource(primaryProperties)
    }

    @Bean
    fun readOnlyDataSource(): DataSource {
        readOnlyProperties.username = dataSourceProperties.username
        readOnlyProperties.password = dataSourceProperties.password
        readOnlyProperties.isReadOnly = true

        return HikariDataSource(readOnlyProperties)
    }
}
