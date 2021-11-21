package com.leijendary.spring.boot.template.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.leijendary.spring.boot.template.client")
public class FeignConfiguration {
}
