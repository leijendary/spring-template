package com.leijendary.spring.microservicetemplate.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Component
public class AppSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public @NonNull Optional<String> getCurrentAuditor() {
        final var authentication = getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        return Optional.of(authentication.getName());
    }
}
