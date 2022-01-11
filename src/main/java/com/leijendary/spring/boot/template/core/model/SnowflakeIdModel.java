package com.leijendary.spring.boot.template.core.model;

import static com.leijendary.spring.boot.template.core.generator.SnowflakeIdGenerator.STRATEGY;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class SnowflakeIdModel extends AppModel {

    protected static final String GENERATOR_SNOWFLAKE = "snowflake";

    @Id
    @GeneratedValue(generator = GENERATOR_SNOWFLAKE)
    @GenericGenerator(name = GENERATOR_SNOWFLAKE, strategy = STRATEGY)
    private long id;
}
