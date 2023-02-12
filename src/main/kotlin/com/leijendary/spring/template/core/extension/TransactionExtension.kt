package com.leijendary.spring.template.core.extension

import com.leijendary.spring.template.core.util.SpringContext.Companion.getBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

private val transactionManager = getBean(PlatformTransactionManager::class)

/**
 * Use this function if you don't want your whole method to run under a single transaction.
 * This is faster as there could be other functions/methods running under the same
 * transaction but does not necessarily need to run in a transaction.
 */
fun <T> transactional(readOnly: Boolean = false, function: () -> T): T {
    val template = TransactionTemplate(transactionManager)
        .apply {
            isReadOnly = readOnly
        }

    return template.execute { function() }!!
}
