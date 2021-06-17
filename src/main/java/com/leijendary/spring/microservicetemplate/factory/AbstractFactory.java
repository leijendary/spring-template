package com.leijendary.spring.microservicetemplate.factory;

import org.modelmapper.ModelMapper;

import static com.leijendary.spring.microservicetemplate.util.SpringContext.getBean;

public abstract class AbstractFactory {

    protected static final ModelMapper MAPPER = getBean(ModelMapper.class);
}
