package com.leijendary.spring.microservicetemplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LocaleAwareRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
