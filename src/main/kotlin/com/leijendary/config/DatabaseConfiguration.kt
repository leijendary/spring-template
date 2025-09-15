package com.leijendary.config

import com.leijendary.config.DataSourceType.READ_ONLY
import com.leijendary.config.DataSourceType.READ_WRITE
import com.leijendary.config.properties.DataSourcePrimaryProperties
import com.leijendary.config.properties.DataSourceReadOnlyProperties
import com.leijendary.context.RequestContext.userIdOrSystem
import com.leijendary.model.PrefixedIdEntity
import com.zaxxer.hikari.HikariDataSource
import io.github.thibaultmeyer.cuid.CUID
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.stereotype.Component
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionManager
import org.springframework.transaction.support.TransactionSynchronizationManager.*
import org.springframework.transaction.support.TransactionTemplate
import java.util.*
import javax.sql.DataSource

enum class DataSourceType {
    READ_WRITE,
    READ_ONLY
}

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DataSourcePrimaryProperties::class, DataSourceReadOnlyProperties::class)
@EnableJdbcAuditing(auditorAwareRef = "auditorAware")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
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

    @Bean
    fun transactionTemplate(transactionManager: JdbcTransactionManager): TransactionTemplate {
        return TransactionTemplate(transactionManager)
    }

    @Bean
    fun readOnlyTransactionTemplate(transactionManager: JdbcTransactionManager): TransactionTemplate {
        return TransactionTemplate(transactionManager).apply { isReadOnly = true }
    }

    @Bean
    fun transactionManager(transactionManager: JdbcTransactionManager): TransactionManager {
        return transactionManager
    }

    @Bean
    fun auditorAware() = AuditorAware {
        Optional.ofNullable(userIdOrSystem)
    }

    @Bean
    fun prefixedIdBeforeConvertCallback() = BeforeConvertCallback<PrefixedIdEntity> {
        if (it.isNew) {
            it.setId("${it.getIdPrefix()}_${CUID.randomCUID2()}")
        }

        it
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
