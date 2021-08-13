package com.leijendary.spring.microservicetemplate.controller;

import javax.servlet.http.HttpServletResponse;

import static com.leijendary.spring.microservicetemplate.util.RequestContext.getPath;
import static org.springframework.http.HttpHeaders.LOCATION;

public abstract class AbstractController {

    public static final String BASE_API_PATH = "/api";

    protected void locationHeader(final HttpServletResponse response, final Object id) {
        response.setHeader(LOCATION, toLocation(id));
    }

    protected String toLocation(final Object id) {
        var path = getPath();

        if (!path.endsWith("/")) {
            path += "/";
        }

        return path + id;
    }
}
