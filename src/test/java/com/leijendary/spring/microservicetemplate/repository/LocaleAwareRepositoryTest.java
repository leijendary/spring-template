package com.leijendary.spring.microservicetemplate.repository;

import com.leijendary.spring.microservicetemplate.ApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LocaleAwareRepositoryTest extends ApplicationTests {

    @Autowired
    private SampleTableRepository sampleTableRepository;

    @Test
    public void findAllShouldWork() {
        final var list = sampleTableRepository.findTranslatedAll();

        System.out.println(list);
    }
}
