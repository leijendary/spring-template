package com.leijendary.spring.template.core.extension

import kotlinx.coroutines.runBlocking
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization

inline fun afterCommit(crossinline function: suspend () -> Unit) {
    registerSynchronization(object : TransactionSynchronization {
        override fun afterCommit() = runBlocking {
            function()
        }
    })
}
