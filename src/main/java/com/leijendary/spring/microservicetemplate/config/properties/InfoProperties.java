package com.leijendary.spring.microservicetemplate.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "info")
@Data
public class InfoProperties {

    private App app;
    private Api api;

    @Data
    public static class App {

        private String organization;
        private String groupId;
        private String artifactId;
        private String name;
        private String description;
        private String version;
    }

    @Data
    public static class Api {

        private String termsOfServiceUrl;
        private Contact contact;
        private String license;
        private String licenseUrl;

        @SuppressWarnings("rawtypes")
        private List<VendorExtension> vendorExtensions = new ArrayList<>();
    }
}
