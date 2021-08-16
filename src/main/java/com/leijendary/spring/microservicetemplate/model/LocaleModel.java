package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class LocaleModel<R> extends IdentityIdModel {

    @ManyToOne
    @JoinColumn(name = "reference_id")
    private R reference;

    private String language;
    private int ordinal;

    @CreatedDate
    private OffsetDateTime createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private OffsetDateTime lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;
}
