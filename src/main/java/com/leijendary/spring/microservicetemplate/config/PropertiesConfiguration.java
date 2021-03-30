package com.leijendary.spring.microservicetemplate.config;

import com.leijendary.spring.microservicetemplate.config.properties.InfoProperties;
import com.leijendary.spring.microservicetemplate.config.properties.AuthProperties;
import com.leijendary.spring.microservicetemplate.config.properties.CorsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ AuthProperties.class, CorsProperties.class, InfoProperties.class })
public class PropertiesConfiguration {
}
