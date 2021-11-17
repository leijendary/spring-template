package com.leijendary.spring.boot.template.security;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AppSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public @NonNull Optional<String> getCurrentAuditor() {
        final var authentication = getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return empty();
        }

        return of(authentication.getName());
    }
}
