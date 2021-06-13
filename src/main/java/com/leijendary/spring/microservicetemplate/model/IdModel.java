package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import static com.leijendary.spring.microservicetemplate.generator.SnowflakeIdGenerator.STRATEGY;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class IdModel extends AbstractModel {

    protected static final String GENERATOR_SNOWFLAKE = "snowflake";

    @Id
    @GeneratedValue(generator = GENERATOR_SNOWFLAKE)
    @GenericGenerator(name = GENERATOR_SNOWFLAKE, strategy = STRATEGY)
    private long id;
}
