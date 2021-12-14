package com.leijendary.spring.boot.template.filter;
import static java.lang.String.join;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.springframework.web.cors.CorsConfiguration.ALL;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.leijendary.spring.boot.template.config.properties.CorsProperties;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityHeaderFilter extends OncePerRequestFilter {

    private final CorsProperties corsProperties;

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {
        final var allowOrigin = allowOrigin();
        final var allowMethods = allowMethods();
        final var maxAge = corsProperties.getMaxAge();
        final var allowHeaders = allowHeaders();
        final var exposeHeaders = exposeHeaders();
        final var isAllowCredentials = corsProperties.isAllowCredentials();

        response.setHeader("Access-Control-Allow-Origin", allowOrigin);
        response.setHeader("Access-Control-Allow-Methods", allowMethods);
        response.setHeader("Access-Control-Max-Age", Long.toString(maxAge));
        response.setHeader("Access-Control-Allow-Headers", allowHeaders);
        response.setHeader("Access-Control-Expose-Headers", exposeHeaders);
        response.setHeader("Access-Control-Allow-Credentials", Boolean.toString(isAllowCredentials));

        chain.doFilter(request, response);
    }

    private String allowOrigin() {
        final var allowedPatterns = corsProperties.getAllowedOriginPatterns();

        if (allowedPatterns.contains(ALL)) {
            return ALL;
        }

        final var allowedOrigins = corsProperties.getAllowedOrigins();
        final var origins = concat(allowedOrigins.stream(), allowedPatterns.stream())
                .collect(toList());

        return join(", ", origins);
    }

    private String allowMethods() {
        final var allowedMethods = corsProperties.getAllowedMethods()
                .stream()
                .map(method -> method.name())
                .collect(toList());

        return join(", ", allowedMethods);
    }

    private String allowHeaders() {
        final var allowedHeaders = corsProperties.getAllowedHeaders();

        return join(", ", allowedHeaders);
    }

    private String exposeHeaders() {
        final var exposeHeaders = corsProperties.getExposedHeaders();

        return join(", ", exposeHeaders);
    }
}
