package com.leijendary.spring.microservicetemplate.controller;

import javax.servlet.http.HttpServletResponse;

import static com.leijendary.spring.microservicetemplate.util.RequestContextUtil.getPath;
import static org.springframework.http.HttpHeaders.LOCATION;

public abstract class AbstractController {

    protected void locationHeader(final HttpServletResponse response, final int id) {
        response.setHeader(LOCATION, toLocation(id));
    }

    protected String toLocation(final int id) {
        var path = getPath();

        if (!path.endsWith("/")) {
            path += "/";
        }

        return path + id;
    }
}
