package com.leijendary.config

import com.leijendary.config.DataSourceType.READ_ONLY
import com.leijendary.config.DataSourceType.READ_WRITE
import com.leijendary.config.properties.DataSourcePrimaryProperties
import com.leijendary.config.properties.DataSourceReadOnlyProperties
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.stereotype.Component
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.TransactionSynchronizationManager.*
import org.springframework.transaction.support.TransactionTemplate
import javax.sql.DataSource

enum class DataSourceType {
    READ_WRITE,
    READ_ONLY
}

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DataSourcePrimaryProperties::class, DataSourceReadOnlyProperties::class)
class DatabaseConfiguration {
    @Bean
    @Primary
    fun dataSource(primaryDataSource: DataSource, readOnlyDataSource: DataSource): DataSource {
        return TransactionRoutingDataSource(primaryDataSource, readOnlyDataSource)
    }

    @Bean
    fun primaryDataSource(config: DataSourcePrimaryProperties): DataSource {
        return HikariDataSource(config)
    }

    @Bean
    fun readOnlyDataSource(config: DataSourceReadOnlyProperties): DataSource {
        return HikariDataSource(config)
    }

    @Primary
    @Bean
    fun transactionTemplate(transactionManager: JdbcTransactionManager): TransactionTemplate {
        return TransactionTemplate(transactionManager)
    }

    @Bean(BEAN_READ_ONLY_TRANSACTION_TEMPLATE)
    fun readOnlyTransactionTemplate(transactionManager: JdbcTransactionManager): TransactionTemplate {
        return TransactionTemplate(transactionManager).apply { isReadOnly = true }
    }

    companion object {
        const val BEAN_READ_ONLY_TRANSACTION_TEMPLATE = "readOnlyTransactionTemplate"
    }
}

@Component
@Primary
class SynchronizedTransactionManager(dataSource: DataSource) : JdbcTransactionManager(dataSource) {
    override fun doBegin(transaction: Any, definition: TransactionDefinition) {
        setCurrentTransactionName(definition.name)
        setCurrentTransactionIsolationLevel(definition.isolationLevel)
        setCurrentTransactionReadOnly(definition.isReadOnly)

        super.doBegin(transaction, definition)
    }
}

class TransactionRoutingDataSource(primary: DataSource, readOnly: DataSource) : AbstractRoutingDataSource() {
    init {
        val map = mapOf<Any, Any>(
            READ_WRITE to primary,
            READ_ONLY to readOnly
        )

        setTargetDataSources(map)
        setDefaultTargetDataSource(primary)
    }

    override fun determineCurrentLookupKey(): Any {
        val isReadOnly = isCurrentTransactionReadOnly()

        return if (isReadOnly) READ_ONLY else READ_WRITE
    }
}
