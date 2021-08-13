package com.leijendary.spring.microservicetemplate.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leijendary.spring.microservicetemplate.config.properties.AuthProperties;
import com.leijendary.spring.microservicetemplate.data.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.leijendary.spring.microservicetemplate.util.RequestContext.getLocale;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public static final String SOURCE = "Authorization";

    private final AuthProperties authProperties;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;

    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException exception) throws IOException {
        log.error("AuthenticationException", exception);

        var code = "access.unauthorized";

        if (exception instanceof OAuth2AuthenticationException) {
            final var oauth2Exception = (OAuth2AuthenticationException) exception;
            final var oAuth2Error = oauth2Exception.getError();
            final var description = oAuth2Error.getDescription();

            if (!isBlank(description)) {
                if (description.contains("expired")) {
                    code = "access.expired";
                } else if (description.contains("Invalid signature")) {
                    code = "access.invalid";
                }
            }
        }

        final var errorResponse = buildResponse(code);
        final var meta = errorResponse.getMeta();
        final var wwwAuthenticate = computeWwwAuthenticateHeaderValue(meta);
        final var json = objectMapper.writeValueAsString(errorResponse);

        response.addHeader("WWW-Authenticate", wwwAuthenticate);
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(json);
        response.flushBuffer();
    }

    private ErrorResponse buildResponse(final String code) {
        final var message = messageSource.getMessage(code, new Object[0], getLocale());
        final var realm = authProperties.getRealm();

        return ErrorResponse.builder()
                .addError(SOURCE, code, message)
                .meta("realm", realm)
                .status(UNAUTHORIZED)
                .build();
    }

    private static String computeWwwAuthenticateHeaderValue(final Map<String, Object> parameters) {
        final var wwwAuthenticate = new StringBuilder("Bearer");

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
