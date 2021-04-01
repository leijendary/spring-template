package com.leijendary.spring.microservicetemplate.security;

import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

public class ScopeWebSecurityExpressionHandler extends DefaultWebSecurityExpressionHandler {

    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Override
    protected ScopeSecurityExpressionOperations createSecurityExpressionRoot(
            final Authentication authentication, final FilterInvocation invocation) {
        final var root = new ScopeSecurityExpressionRootMethod(authentication);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());

        return root;
    }

    @Override
    protected StandardEvaluationContext createEvaluationContextInternal(
            final Authentication authentication, final FilterInvocation invocation) {
        final var root = this.createSecurityExpressionRoot(authentication, invocation);
        final var context = super.createEvaluationContextInternal(authentication, invocation);
        context.setRootObject(root);

        return context;
    }
}
