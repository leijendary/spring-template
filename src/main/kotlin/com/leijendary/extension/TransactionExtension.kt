package com.leijendary.extension

import com.leijendary.util.readOnlyTransactionalTemplate
import com.leijendary.util.transactionTemplate
import org.springframework.transaction.TransactionStatus

/**
 * Use this function if you don't want your whole method to run under a single transaction.
 * This is faster as there could be other functions/methods running under the same
 * transaction but does not necessarily need to run in a transaction.
 */
fun <T> transactional(readOnly: Boolean = false, function: (TransactionStatus) -> T): T {
    val template = if (readOnly) readOnlyTransactionalTemplate else transactionTemplate

    return template.execute(function)!!
}
