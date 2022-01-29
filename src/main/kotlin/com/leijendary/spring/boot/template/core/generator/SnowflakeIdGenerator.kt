package com.leijendary.spring.boot.template.core.generator

import com.leijendary.spring.boot.template.core.worker.SnowflakeIdWorker
import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import java.io.Serializable

class SnowflakeIdGenerator : IdentifierGenerator {
    companion object {
        const val STRATEGY = "com.leijendary.spring.boot.template.generator.SnowflakeIdGenerator"
        private val WORKER: SnowflakeIdWorker = SnowflakeIdWorker()
    }

    @Throws(HibernateException::class)
    override fun generate(session: SharedSessionContractImplementor, any: Any): Serializable {
        return WORKER.nextId()
    }
}