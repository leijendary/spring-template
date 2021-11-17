package com.leijendary.spring.boot.template.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(CacheManager.class)
@EnableCaching
public class CacheAutoConfiguration {
}
