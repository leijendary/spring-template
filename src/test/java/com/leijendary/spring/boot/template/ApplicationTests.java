package com.leijendary.spring.boot.template;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class ApplicationTests {

    @Test
    void contextLoads() {
    }
}
