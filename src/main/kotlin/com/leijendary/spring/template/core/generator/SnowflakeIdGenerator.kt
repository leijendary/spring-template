package com.leijendary.spring.template.core.generator

import com.leijendary.spring.template.core.worker.SnowflakeIdWorker
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import java.io.Serializable

class SnowflakeIdGenerator : IdentifierGenerator {
    companion object {
        const val STRATEGY = "com.leijendary.spring.template.core.generator.SnowflakeIdGenerator"
        private val WORKER: SnowflakeIdWorker = SnowflakeIdWorker()
    }

    override fun generate(session: SharedSessionContractImplementor, any: Any): Serializable {
        return WORKER.nextId()
    }
}
