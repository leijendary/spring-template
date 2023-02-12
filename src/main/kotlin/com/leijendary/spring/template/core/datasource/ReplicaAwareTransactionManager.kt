package com.leijendary.spring.template.core.datasource

import com.leijendary.spring.template.core.datasource.TransactionRoutingDataSource.Companion.setReadonlyDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus

class ReplicaAwareTransactionManager(private val manager: JpaTransactionManager) : PlatformTransactionManager {
    override fun getTransaction(definition: TransactionDefinition?): TransactionStatus {
        setReadonlyDataSource(definition!!.isReadOnly)

        return manager.getTransaction(definition)
    }

    override fun commit(status: TransactionStatus) = manager.commit(status)

    override fun rollback(status: TransactionStatus) = manager.rollback(status)
}
