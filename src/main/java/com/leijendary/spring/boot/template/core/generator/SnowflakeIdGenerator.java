package com.leijendary.spring.boot.template.core.generator;

import java.io.Serializable;

import com.leijendary.spring.boot.template.core.worker.SnowflakeIdWorker;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class SnowflakeIdGenerator implements IdentifierGenerator {

    public static final String STRATEGY = "com.leijendary.spring.boot.template.generator.SnowflakeIdGenerator";

    private static final SnowflakeIdWorker WORKER = new SnowflakeIdWorker();

    @Override
    public Serializable generate(final SharedSessionContractImplementor session, final Object object)
            throws HibernateException {
        return WORKER.nextId();
    }
}
