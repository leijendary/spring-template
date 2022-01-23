package com.leijendary.spring.boot.template.repository;

import static javax.persistence.LockModeType.PESSIMISTIC_FORCE_INCREMENT;
import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import javax.persistence.QueryHint;

import com.leijendary.spring.boot.template.core.repository.SoftDeleteRepository;
import com.leijendary.spring.boot.template.model.SampleTable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

public interface SampleTableRepository extends JpaRepository<SampleTable, UUID>, JpaSpecificationExecutor<SampleTable>,
        SoftDeleteRepository<SampleTable> {

    Page<SampleTable> findAll(final Specification<SampleTable> specification, final Pageable pageable);

    @Lock(PESSIMISTIC_FORCE_INCREMENT)
    Optional<SampleTable> findLockedById(final UUID id);

    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1"))
    @Query("select s from SampleTable s where s.deletedAt is null")
    Stream<SampleTable> streamAll();
}
