package com.leijendary.spring.boot.template.controller;

import static com.leijendary.spring.boot.template.util.RequestContext.getPath;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.LOCATION;

import javax.servlet.http.HttpServletResponse;

public abstract class AppController {

    public static final String BASE_API_PATH = "/api";

    protected void locationHeader(final HttpServletResponse response, final Object id) {
        response.setHeader(LOCATION, toLocation(id));
    }

    protected String toLocation(final Object id) {
        return ofNullable(getPath())
                .map(path -> {
                    if (!path.endsWith("/")) {
                        return path + "/";
                    }

                    return path;
                })
                .map(path -> path + id)
                .orElse("");
    }
}
