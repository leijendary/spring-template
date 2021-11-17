package com.leijendary.spring.boot.template.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@ConditionalOnClass(AuthenticationManager.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityAutoConfiguration extends GlobalMethodSecurityConfiguration {
}
