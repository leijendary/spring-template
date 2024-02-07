package com.leijendary.extension

import com.leijendary.util.transactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionTemplate

/**
 * Use this function if you don't want your whole method to run under a single transaction.
 * This is faster as there could be other functions/methods running under the same
 * transaction but does not necessarily need to run in a transaction.
 */
fun <T> transactional(readOnly: Boolean = false, function: (TransactionStatus) -> T): T {
    val template = TransactionTemplate(transactionManager).apply { isReadOnly = readOnly }

    return template.execute(function)!!
}
