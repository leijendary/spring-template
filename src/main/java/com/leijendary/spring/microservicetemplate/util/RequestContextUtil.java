package com.leijendary.spring.microservicetemplate.util;

import org.springframework.util.Assert;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import static java.util.Optional.ofNullable;
import static org.springframework.web.context.request.RequestContextHolder.getRequestAttributes;

public class RequestContextUtil {

    public static HttpServletRequest getCurrentRequest() {
        final var attributes = getRequestAttributes();

        Assert.state(attributes instanceof ServletRequestAttributes, "No current ServletRequestAttributes");

        return ((ServletRequestAttributes) attributes).getRequest();
    }

    public static String getPath() {
        return getCurrentRequest().getRequestURI();
    }

    public static TimeZone getTimeZone() {
        return ofNullable(RequestContextUtils.getTimeZone(getCurrentRequest()))
                .orElse(TimeZone.getDefault());
    }

    public static Locale getLocale() {
        return Optional.of(RequestContextUtils.getLocale(getCurrentRequest()))
                .orElse(Locale.getDefault());
    }
}
