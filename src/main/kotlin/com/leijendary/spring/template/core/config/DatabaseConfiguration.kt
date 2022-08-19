package com.leijendary.spring.template.core.config

import com.leijendary.spring.template.core.config.DatabaseConfiguration.DataSourceType.READ_ONLY
import com.leijendary.spring.template.core.config.DatabaseConfiguration.DataSourceType.READ_WRITE
import com.leijendary.spring.template.core.config.properties.DataSourcePrimaryProperties
import com.leijendary.spring.template.core.config.properties.DataSourceReadonlyProperties
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
        READ_WRITE,
        READ_ONLY
    }

    inner class TransactionRoutingDataSource : AbstractRoutingDataSource() {
        override fun determineCurrentLookupKey(): Any {
            val isReadOnly = isCurrentTransactionReadOnly()

            return if (isReadOnly) READ_ONLY else READ_WRITE
        }
    }

    @Bean
    @Primary
    fun dataSource(): DataSource {
        return LazyConnectionDataSourceProxy(routingDataSource())
    }

    @Bean
    fun routingDataSource(): TransactionRoutingDataSource {
        val dataSource = mutableMapOf<Any, Any>(
            READ_WRITE to primaryDataSource(),
            READ_ONLY to readonlyDataSource()
        )
        val routing = TransactionRoutingDataSource()
        routing.setTargetDataSources(dataSource)

        return routing
    }

    private fun primaryDataSource(): HikariDataSource {
        return HikariDataSource(primaryProperties)
    }

    private fun readonlyDataSource(): HikariDataSource {
        return HikariDataSource(readonlyProperties)
    }
}