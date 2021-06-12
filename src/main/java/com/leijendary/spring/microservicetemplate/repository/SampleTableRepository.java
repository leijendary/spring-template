package com.leijendary.spring.microservicetemplate.repository;

import com.leijendary.spring.microservicetemplate.model.SampleTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SampleTableRepository extends JpaRepository<SampleTable, Long>, JpaSpecificationExecutor<SampleTable> {
}
