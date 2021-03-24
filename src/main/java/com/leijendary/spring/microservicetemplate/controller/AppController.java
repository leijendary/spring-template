package com.leijendary.spring.microservicetemplate.controller;

import static com.leijendary.spring.microservicetemplate.util.RequestContextUtil.getPath;

public abstract class AppController {

    public String toLocation(int id) {
        String path = getPath();

        if (!path.endsWith("/")) {
            path += "/";
        }

        return path + id;
    }
}
