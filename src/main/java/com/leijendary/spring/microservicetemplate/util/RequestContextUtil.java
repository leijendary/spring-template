package com.leijendary.spring.microservicetemplate.util;

import org.springframework.util.Assert;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import static java.util.Optional.ofNullable;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static org.springframework.web.context.request.RequestContextHolder.getRequestAttributes;

public class RequestContextUtil {

    public static HttpServletRequest getCurrentRequest() {
        final var attributes = getRequestAttributes();

        Assert.state(attributes instanceof ServletRequestAttributes, "No current ServletRequestAttributes");

        return ((ServletRequestAttributes) attributes).getRequest();
    }

    public static String getUsername() {
        return getContext().getAuthentication().getName();
    }

    public static String getPath() {
        return getCurrentRequest().getRequestURI();
    }

    public static URI uri() {
        var uri = getCurrentRequest().getRequestURI();
        final var params = getCurrentRequest().getQueryString();

        if (Optional.ofNullable(params).isPresent()) {
            uri += "?" + params;
        }

        return URI.create(uri);
    }

    public static TimeZone getTimeZone() {
        return ofNullable(RequestContextUtils.getTimeZone(getCurrentRequest()))
                .orElse(TimeZone.getDefault());
    }

    public static Locale getLocale() {
        return Optional.of(RequestContextUtils.getLocale(getCurrentRequest()))
                .orElse(Locale.getDefault());
    }

    public static String getLanguage() {
        return getLocale().getLanguage();
    }

    public static OffsetDateTime now() {
        return OffsetDateTime.now(getTimeZone().toZoneId());
    }
}
