package com.leijendary.spring.microservicetemplate.util;

import org.springframework.lang.Nullable;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.TimeZone;

import static java.net.URI.create;
import static java.util.Optional.ofNullable;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static org.springframework.web.context.request.RequestContextHolder.getRequestAttributes;

public class RequestContext {

    @Nullable
    public static HttpServletRequest getCurrentRequest() {
        final var attributes = getRequestAttributes();

        if (!(attributes instanceof ServletRequestAttributes)) {
            return null;
        }

        return ((ServletRequestAttributes) attributes).getRequest();
    }

    @Nullable
    public static String getUsername() {
        return getContext().getAuthentication().getName();
    }

    @Nullable
    public static String getPath() {
        final var request = getCurrentRequest();

        if (request == null) {
            return null;
        }

        final var contextPath = request.getContextPath();

        return request.getRequestURI().replaceFirst(contextPath, "");
    }

    @Nullable
    public static URI uri() {
        final var request = getCurrentRequest();
        var path = getPath();

        if (request == null || path == null) {
            return null;
        }

        final var params = request.getQueryString();

        if (ofNullable(params).isPresent()) {
            path += "?" + params;
        }

        return create(path);
    }

    public static TimeZone getTimeZone() {
        return ofNullable(getCurrentRequest())
                .map(RequestContextUtils::getTimeZone)
                .orElse(TimeZone.getDefault());
    }

    public static Locale getLocale() {
        return ofNullable(getCurrentRequest())
                .map(RequestContextUtils::getLocale)
                .orElse(Locale.getDefault());
    }

    public static String getLanguage() {
        return getLocale().getLanguage();
    }

    public static OffsetDateTime now() {
        return OffsetDateTime.now(getTimeZone().toZoneId());
    }
}
