package com.leijendary.spring.microservicetemplate.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leijendary.spring.microservicetemplate.config.properties.AuthProperties;
import com.leijendary.spring.microservicetemplate.util.RequestContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Locale.getDefault;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuthProperties authProperties;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;

    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException exception) throws IOException {
        final var description = messageSource.getMessage("access.unauthorized", new Object[0], getDefault());
        final var error = messageSource.getMessage("access.error", new Object[0], getDefault());
        var status = UNAUTHORIZED;
        final var parameters = new LinkedHashMap<String, Object>();

        if (authProperties.getRealm() != null) {
            parameters.put("realm", authProperties.getRealm());
        }

        if (exception instanceof OAuth2AuthenticationException) {
            final var oAuth2Error = ((OAuth2AuthenticationException) exception).getError();

            parameters.put("error", oAuth2Error.getErrorCode());

            if (StringUtils.hasText(oAuth2Error.getDescription())) {
                parameters.put("message", oAuth2Error.getDescription());
            }

            if (StringUtils.hasText(oAuth2Error.getUri())) {
                parameters.put("uri", oAuth2Error.getUri());
            }

            if (oAuth2Error instanceof BearerTokenError) {
                final var bearerTokenError = (BearerTokenError) oAuth2Error;

                if (StringUtils.hasText(bearerTokenError.getScope())) {
                    parameters.put("scope", bearerTokenError.getScope());
                }

                status = ((BearerTokenError) oAuth2Error).getHttpStatus();
            }
        }

        final var wwwAuthenticate = computeWwwAuthenticateHeaderValue(parameters);

        parameters.put("timestamp", Instant.now());
        parameters.put("path", RequestContextUtil.getPath());
        parameters.put("status", status.value());

        if (!parameters.containsKey("error")) {
            parameters.put("error", error);
        }

        if (!parameters.containsKey("message")) {
            parameters.put("message", description);
        }

        final var json = objectMapper.writeValueAsString(parameters);

        response.addHeader("WWW-Authenticate", wwwAuthenticate);
        response.setStatus(status.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(json);
        response.flushBuffer();
    }

    private static String computeWwwAuthenticateHeaderValue(final Map<String, Object> parameters) {
        final var wwwAuthenticate = new StringBuilder();
        wwwAuthenticate.append("Bearer");

        if (parameters.isEmpty()) {
            return wwwAuthenticate.toString();
        }

        wwwAuthenticate.append(" ");

        var i = 0;

        for (final var iterator = parameters.entrySet().iterator(); iterator.hasNext(); ++i) {
            final var entry = (Map.Entry<String, Object>) iterator.next();

            wwwAuthenticate.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");

            if (i != parameters.size() - 1) {
                wwwAuthenticate.append(", ");
            }
        }

        return wwwAuthenticate.toString();
    }
}
