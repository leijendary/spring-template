package com.leijendary.spring.template.core.datasource

import com.leijendary.spring.template.core.datasource.DataSourceType.READ_ONLY
import com.leijendary.spring.template.core.datasource.DataSourceType.READ_WRITE
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import javax.sql.DataSource

enum class DataSourceType {
    READ_WRITE,
    READ_ONLY
}

class TransactionRoutingDataSource(primary: DataSource, readonly: DataSource) : AbstractRoutingDataSource() {
    init {
        val dataSource = mutableMapOf<Any, Any>(
            READ_WRITE to primary,
            READ_ONLY to readonly
        )

        this.setTargetDataSources(dataSource)
        this.setDefaultTargetDataSource(primary)
    }

    companion object {
        private val currentDataSource = ThreadLocal.withInitial { READ_WRITE }

        fun setReadonlyDataSource(isReadonly: Boolean) {
            val type = if (isReadonly) READ_ONLY else READ_WRITE

            currentDataSource.set(type)
        }
    }

    override fun determineCurrentLookupKey(): DataSourceType = currentDataSource.get()
}
