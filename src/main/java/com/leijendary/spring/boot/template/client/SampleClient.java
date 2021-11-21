package com.leijendary.spring.boot.template.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("google")
public interface SampleClient {

    @GetMapping
    String homepage();
}
