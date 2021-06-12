package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class SnowflakeIdModel extends AbstractModel {

    protected static final String GENERATOR_SNOWFLAKE = "snowflake";

    @Id
    @GenericGenerator(
            name = "snowflake",
            strategy = "com.leijendary.spring.microservicetemplate.model.generator.SnowflakeSequenceGenerator")
    @GeneratedValue(generator = GENERATOR_SNOWFLAKE)
    private long id;
}
