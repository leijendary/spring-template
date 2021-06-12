package com.leijendary.spring.microservicetemplate.model.generator;

import com.leijendary.spring.microservicetemplate.worker.SnowflakeIdWorker;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class SnowflakeSequenceGenerator implements IdentifierGenerator {

    private static final SnowflakeIdWorker WORKER = new SnowflakeIdWorker();

    @Override
    public Serializable generate(final SharedSessionContractImplementor session,
                                 final Object object) throws HibernateException {
        return WORKER.nextId();
    }
}
