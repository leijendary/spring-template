package com.leijendary.spring.microservicetemplate.model.listener;

import static org.springframework.web.context.support.SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext;

public abstract class AppEntityListener {

    protected AppEntityListener() {
        processInjectionBasedOnCurrentContext(this);
    }
}
