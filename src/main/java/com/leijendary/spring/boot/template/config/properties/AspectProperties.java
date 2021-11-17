package com.leijendary.spring.boot.template.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "aspect")
@Data
public class AspectProperties {

    private Logging logging;
    private Execution execution;

    @Data
    public static class Logging {

        private boolean enabled;
    }

    @Data
    public static class Execution {

        private long time = -1;
    }
}
