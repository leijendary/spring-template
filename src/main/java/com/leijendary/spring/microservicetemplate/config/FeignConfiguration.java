package com.leijendary.spring.microservicetemplate.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.leijendary.spring.microservicetemplate.client")
public class FeignConfiguration {
}
