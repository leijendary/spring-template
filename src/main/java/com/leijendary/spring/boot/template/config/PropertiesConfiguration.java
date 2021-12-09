package com.leijendary.spring.boot.template.config;

import com.leijendary.spring.boot.template.config.properties.AuthProperties;
import com.leijendary.spring.boot.template.config.properties.CorsProperties;
import com.leijendary.spring.boot.template.config.properties.InfoProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        AuthProperties.class,
        CorsProperties.class,
        InfoProperties.class
})
public class PropertiesConfiguration {
}
