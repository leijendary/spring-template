package com.leijendary.spring.boot.template.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(DiscoveryClient.class)
@EnableDiscoveryClient
public class DiscoveryAutoConfiguration {
}
