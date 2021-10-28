package com.leijendary.spring.microservicetemplate.repository;

import com.leijendary.spring.microservicetemplate.model.SampleTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SampleTableRepository extends JpaRepository<SampleTable, UUID>, JpaSpecificationExecutor<SampleTable> {

    boolean existsByColumn1IgnoreCaseAndIdNot(final String column1, final UUID id);

    boolean existsByColumn1IgnoreCase(final String column1);
}
