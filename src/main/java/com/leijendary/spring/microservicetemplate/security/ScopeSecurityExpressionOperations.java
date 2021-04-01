package com.leijendary.spring.microservicetemplate.security;

import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;

public interface ScopeSecurityExpressionOperations extends MethodSecurityExpressionOperations {

    boolean hasScope(String scope);

    boolean hasAnyScope(String... scopes);
}
