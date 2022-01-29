package com.leijendary.spring.boot.template.core.model

import com.leijendary.spring.boot.template.core.generator.SnowflakeIdGenerator.Companion.STRATEGY
import org.hibernate.annotations.GenericGenerator
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class SnowflakeIdModel : AppModel() {
    companion object {
        protected const val GENERATOR_SNOWFLAKE = "snowflake"
    }

    @Id
    @GeneratedValue(generator = GENERATOR_SNOWFLAKE)
    @GenericGenerator(name = GENERATOR_SNOWFLAKE, strategy = STRATEGY)
    var id: Long = 0
}