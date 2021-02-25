package com.leijendary.spring.microservicetemplate.config;

import com.leijendary.spring.microservicetemplate.config.properties.ApplicationProperties;
import com.leijendary.spring.microservicetemplate.config.properties.CorsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ ApplicationProperties.class, CorsProperties.class})
public class PropertiesConfiguration {
}
