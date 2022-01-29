package com.leijendary.spring.boot.template.core.config

import com.leijendary.spring.boot.template.core.config.DatabaseConfiguration.DataSourceType.PRIMARY
import com.leijendary.spring.boot.template.core.config.DatabaseConfiguration.DataSourceType.READONLY
import com.leijendary.spring.boot.template.core.config.properties.DataSourcePrimaryProperties
import com.leijendary.spring.boot.template.core.config.properties.DataSourceReadonlyProperties
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager.isCurrentTransactionReadOnly
import javax.sql.DataSource

@Configuration
class DatabaseConfiguration(
    private val primaryProperties: DataSourcePrimaryProperties,
    private val readonlyProperties: DataSourceReadonlyProperties
) {
    enum class DataSourceType {
        PRIMARY,
        READONLY
    }

    inner class TransactionRoutingDataSource : AbstractRoutingDataSource() {
        override fun determineCurrentLookupKey(): Any {
            val isReadOnly = isCurrentTransactionReadOnly()

            return if (isReadOnly) READONLY else PRIMARY
        }
    }

    @Bean
    @Primary
    fun dataSource(): DataSource {
        return LazyConnectionDataSourceProxy(routingDataSource())
    }

    @Bean
    fun routingDataSource(): TransactionRoutingDataSource {
        val routing = TransactionRoutingDataSource()
        val dataSource = HashMap<Any, Any>()
        dataSource[PRIMARY] = primaryDataSource()
        dataSource[READONLY] = readonlyDataSource()

        routing.setTargetDataSources(dataSource)

        return routing
    }

    @Bean
    fun primaryDataSource(): HikariDataSource {
        return HikariDataSource(primaryProperties)
    }

    @Bean
    fun readonlyDataSource(): HikariDataSource {
        return HikariDataSource(readonlyProperties)
    }
}