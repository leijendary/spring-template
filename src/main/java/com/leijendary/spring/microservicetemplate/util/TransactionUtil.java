package com.leijendary.spring.microservicetemplate.util;

import org.springframework.transaction.support.TransactionSynchronization;

import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

public class TransactionUtil {

    /**
     * Use this method to execute a {@link Runnable} after the transaction has been committed
     * since nested IDs of an entity is not generated in @PostPersist.
     *
     * @param runnable The Runnable function that will be executed after commit
     */
    public static void afterCommit(final Runnable runnable) {
        registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                runnable.run();
            }
        });
    }
}
