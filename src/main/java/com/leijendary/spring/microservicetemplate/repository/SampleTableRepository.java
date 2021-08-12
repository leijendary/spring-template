package com.leijendary.spring.microservicetemplate.repository;

import com.leijendary.spring.microservicetemplate.model.SampleTable;
import com.leijendary.spring.microservicetemplate.model.SampleTableTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SampleTableRepository extends JpaRepository<SampleTable, Long>, JpaSpecificationExecutor<SampleTable>,
        LocaleAwareRepository<SampleTable, SampleTableTranslation, Long> {

    Optional<SampleTable> findFirstByColumn1IgnoreCaseAndIdNot(final String column1, final long id);
}
