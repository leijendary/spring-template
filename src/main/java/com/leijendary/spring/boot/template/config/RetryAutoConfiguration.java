package com.leijendary.spring.boot.template.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.RetryConfiguration;

@Configuration
@ConditionalOnBean(RetryConfiguration.class)
@EnableRetry
public class RetryAutoConfiguration {
}
