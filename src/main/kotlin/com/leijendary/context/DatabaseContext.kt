package com.leijendary.context

import com.leijendary.config.DatabaseConfiguration.Companion.BEAN_READ_ONLY_TRANSACTION_TEMPLATE
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization
import org.springframework.transaction.support.TransactionTemplate

@Component
class DatabaseContext(
    @Qualifier(BEAN_READ_ONLY_TRANSACTION_TEMPLATE)
    private val readOnlyTransactionalTemplate: TransactionTemplate,

    private val transactionTemplate: TransactionTemplate
) {
    /**
     * Use this function if you don't want your whole method to run under a single transaction.
     * This is faster as there could be other functions/methods running under the same
     * transaction but does not necessarily need to run in a transaction.
     */
    fun <T> transactional(readOnly: Boolean = false, function: (TransactionStatus) -> T): T {
        val template = if (readOnly) readOnlyTransactionalTemplate else transactionTemplate

        return template.execute(function)!!
    }

    companion object {
        /**
         * Use this function when the code is inside a transaction, and you want to run something when the transaction ends.
         */
        fun afterCommit(function: () -> Any) = registerSynchronization(object : TransactionSynchronization {
            override fun afterCommit() {
                function()
            }
        })
    }
}
