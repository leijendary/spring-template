package com.leijendary.spring.microservicetemplate.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

public class ScopeSecurityExpressionRootMethod extends SecurityExpressionRoot
        implements ScopeSecurityExpressionOperations {

    private static final String PREFIX = "SCOPE_";

    private Object filterObject;
    private Object returnObject;

    /**
     * Creates a new instance
     *
     * @param authentication the {@link Authentication} to use. Cannot be null.
     */
    public ScopeSecurityExpressionRootMethod(Authentication authentication) {
        super(authentication);
    }

    public boolean hasScope(String scope) {
        return hasAnyScope(getScopeWithPrefix(scope));
    }

    @Override
    public boolean hasAnyScope(String... scopes) {
        for (int i = 0; i < scopes.length; i++) {
            scopes[i] = getScopeWithPrefix(scopes[i]);
        }

        return hasAnyAuthority(scopes);
    }

    private static String getScopeWithPrefix(final String scope) {
        if (scope.startsWith(PREFIX)) {
            return scope;
        }

        return PREFIX + scope;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
