package com.leijendary.spring.microservicetemplate.repository;

import com.leijendary.spring.microservicetemplate.model.SampleTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SampleTableRepository extends JpaRepository<SampleTable, Long>, JpaSpecificationExecutor<SampleTable> {

    Optional<SampleTable> findFirstByColumn1IgnoreCaseAndIdNot(final String column1, final long id);
}
