package com.leijendary.spring.boot.template.core.config;

import com.leijendary.spring.boot.template.core.config.properties.AuthProperties;
import com.leijendary.spring.boot.template.core.config.properties.InfoProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ AuthProperties.class, InfoProperties.class })
public class PropertiesConfiguration {
}
