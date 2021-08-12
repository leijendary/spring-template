package com.leijendary.spring.microservicetemplate.repository;

import com.leijendary.spring.microservicetemplate.model.SampleTable;
import com.leijendary.spring.microservicetemplate.model.SampleTableTranslation;

import java.util.Optional;

public interface SampleTableRepository extends LocaleAwareRepository<SampleTable, SampleTableTranslation, Long> {

    Optional<SampleTable> findFirstByColumn1IgnoreCaseAndIdNot(final String column1, final long id);
}
