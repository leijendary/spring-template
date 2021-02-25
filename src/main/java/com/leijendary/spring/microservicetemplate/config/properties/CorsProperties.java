package com.leijendary.spring.microservicetemplate.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "cors")
@Data
public class CorsProperties {

    private List<String> allowedOrigins = new ArrayList<>();
    private List<String> allowedOriginPatterns = new ArrayList<>();
    private List<String> allowedHeaders = new ArrayList<>();
    private List<String> exposedHeaders = new ArrayList<>();
    private List<HttpMethod> allowedMethods = new ArrayList<>();
    private boolean allowCredentials = false;
    private long maxAge = 1800;
}
