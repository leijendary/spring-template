package com.leijendary.spring.microservicetemplate.factory;

import com.leijendary.spring.microservicetemplate.data.AppPage;
import org.springframework.data.domain.Page;

public class AppPageFactory {

    public static <T> AppPage<T> of(Page<T> page) {
        return AppPage.of(page.getContent(), page.getPageable(), page.getTotalElements());
    }
}
