package com.leijendary.spring.microservicetemplate.util;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class RequestContextUtil {

    public static String getPath() {
        return getCurrentRequest().getPathInfo();
    }

    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        Assert.state(attributes instanceof ServletRequestAttributes, "No current ServletRequestAttributes");

        return ((ServletRequestAttributes) attributes).getRequest();
    }
}
