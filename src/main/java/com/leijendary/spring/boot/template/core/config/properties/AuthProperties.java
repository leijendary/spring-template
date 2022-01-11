package com.leijendary.spring.boot.template.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "auth")
@Data
public class AuthProperties {

    private String audience;
    private String realm;
    private AnonymousUser anonymousUser;

    @Data
    public static class AnonymousUser {

        private String principal;
    }
}
