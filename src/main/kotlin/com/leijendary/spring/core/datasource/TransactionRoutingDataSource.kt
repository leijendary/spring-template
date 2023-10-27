package com.leijendary.spring.core.datasource

import com.leijendary.spring.core.datasource.DataSourceType.READ_ONLY
import com.leijendary.spring.core.datasource.DataSourceType.READ_WRITE
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.sql.DataSource

enum class DataSourceType {
    READ_WRITE,
    READ_ONLY
}

class TransactionRoutingDataSource(primary: DataSource, readOnly: DataSource) : AbstractRoutingDataSource() {
    init {
        val dataSource = mutableMapOf<Any, Any>(
            READ_WRITE to primary,
            READ_ONLY to readOnly
        )

        this.setTargetDataSources(dataSource)
        this.setDefaultTargetDataSource(primary)
    }

    override fun determineCurrentLookupKey(): DataSourceType {
        val isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly()

        return if (isReadOnly) READ_ONLY else READ_WRITE
    }
}
