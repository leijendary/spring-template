package com.leijendary.spring.microservicetemplate.config;

import com.leijendary.spring.microservicetemplate.security.ScopeMethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Override
    protected ScopeMethodSecurityExpressionHandler createExpressionHandler() {
        return new ScopeMethodSecurityExpressionHandler();
    }
}
