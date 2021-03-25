package com.leijendary.spring.microservicetemplate.util;

import org.springframework.util.Assert;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.util.TimeZone;

import static java.util.Optional.ofNullable;
import static java.util.TimeZone.getDefault;
import static org.springframework.web.context.request.RequestContextHolder.getRequestAttributes;
import static org.springframework.web.servlet.support.RequestContextUtils.getTimeZone;

public class RequestContextUtil {

    public static HttpServletRequest getCurrentRequest() {
        final var attributes = getRequestAttributes();

        Assert.state(attributes instanceof ServletRequestAttributes, "No current ServletRequestAttributes");

        return ((ServletRequestAttributes) attributes).getRequest();
    }

    public static String getPath() {
        return getCurrentRequest().getRequestURI();
    }

    public static ZoneId getZoneId() {
        return ofNullable(getTimeZone(getCurrentRequest()))
                .map(TimeZone::toZoneId)
                .orElse(getDefault().toZoneId());
    }
}
