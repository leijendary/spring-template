package com.leijendary.spring.microservicetemplate.repository;

import com.leijendary.spring.microservicetemplate.model.SampleTableTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SampleTableTranslationRepository extends JpaRepository<SampleTableTranslation, Long>,
        JpaSpecificationExecutor<SampleTableTranslation> {
}
