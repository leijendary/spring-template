package com.leijendary.spring.microservicetemplate.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth")
@Data
public class AuthProperties {

    private String audience;
    private String realm;
}
