package com.leijendary.spring.microservicetemplate.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;

public class ScopeMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    @Override
    protected ScopeSecurityExpressionOperations createSecurityExpressionRoot(
            final Authentication authentication, final MethodInvocation invocation) {
        final var root = new ScopeSecurityExpressionRootMethod(authentication);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(getTrustResolver());
        root.setRoleHierarchy(getRoleHierarchy());

        return root;
    }

    @Override
    public StandardEvaluationContext createEvaluationContextInternal(
            final Authentication authentication, final MethodInvocation invocation) {
        final var root = this.createSecurityExpressionRoot(authentication, invocation);
        final var context = super.createEvaluationContextInternal(authentication, invocation);
        context.setRootObject(root);

        return context;
    }
}
