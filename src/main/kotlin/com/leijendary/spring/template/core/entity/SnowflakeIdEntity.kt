package com.leijendary.spring.template.core.entity

import com.leijendary.spring.template.core.generator.SnowflakeIdGenerator.Companion.STRATEGY
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.GenericGenerator

@MappedSuperclass
open class SnowflakeIdEntity : AppEntity() {
    companion object {
        protected const val GENERATOR_SNOWFLAKE = "snowflake"
    }

    @Id
    @GeneratedValue(generator = GENERATOR_SNOWFLAKE)
    @GenericGenerator(name = GENERATOR_SNOWFLAKE, strategy = STRATEGY)
    var id: Long = 0
}
