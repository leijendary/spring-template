package com.leijendary.spring.microservicetemplate.generator;

import com.leijendary.spring.microservicetemplate.worker.SnowflakeIdWorker;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class SnowflakeIdGenerator implements IdentifierGenerator {

    public static final String STRATEGY = "com.leijendary.spring.microservicetemplate.generator.SnowflakeIdGenerator";

    private static final SnowflakeIdWorker WORKER = new SnowflakeIdWorker();

    @Override
    public Serializable generate(final SharedSessionContractImplementor session, final Object object)
            throws HibernateException {
        return WORKER.nextId();
    }
}
