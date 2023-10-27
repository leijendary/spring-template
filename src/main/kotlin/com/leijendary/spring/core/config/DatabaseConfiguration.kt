package com.leijendary.spring.core.config

import com.leijendary.spring.core.config.properties.DataSourcePrimaryProperties
import com.leijendary.spring.core.config.properties.DataSourceReadOnlyProperties
import com.leijendary.spring.core.datasource.TransactionRoutingDataSource
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class DatabaseConfiguration(
    private val primaryProperties: DataSourcePrimaryProperties,
    private val readOnlyProperties: DataSourceReadOnlyProperties
) {
    @Bean
    @Primary
    fun dataSource(routingDataSource: DataSource): DataSource {
        return LazyConnectionDataSourceProxy(routingDataSource)
    }

    @Bean
    fun routingDataSource(): DataSource {
        val primary = HikariDataSource(primaryProperties)
        val readOnly = HikariDataSource(readOnlyProperties)

        return TransactionRoutingDataSource(primary, readOnly)
    }
}
