package com.leijendary.spring.boot.template.repository;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import javax.persistence.QueryHint;

import com.leijendary.spring.boot.template.model.SampleTable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

public interface SampleTableRepository extends JpaRepository<SampleTable, UUID>, JpaSpecificationExecutor<SampleTable>,
        SoftDeleteRepository<SampleTable> {

    boolean existsByColumn1IgnoreCaseAndIdNot(final String column1, final UUID id);

    boolean existsByColumn1IgnoreCase(final String column1);

    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1"))
    @Query("select s from SampleTable s where s.deletedAt is null")
    Stream<SampleTable> streamAll();
}
