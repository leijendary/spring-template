package com.leijendary.spring.microservicetemplate.factory;

import com.leijendary.spring.microservicetemplate.data.LocalizedData;
import com.leijendary.spring.microservicetemplate.model.LocaleCopy;
import com.leijendary.spring.microservicetemplate.model.LocaleModel;
import com.leijendary.spring.microservicetemplate.model.LocalizedCopy;
import com.leijendary.spring.microservicetemplate.model.LocalizedModel;
import org.modelmapper.ModelMapper;

import static com.leijendary.spring.microservicetemplate.util.SpringContext.getBean;
import static java.util.stream.Collectors.toSet;

public abstract class AbstractFactory {

    protected static final ModelMapper MAPPER = getBean(ModelMapper.class);
}
