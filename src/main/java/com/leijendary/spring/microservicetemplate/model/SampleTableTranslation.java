package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SampleTableTranslation extends LocaleModel<SampleTable> {

    private String name;
    private String description;

    @CreatedDate
    private OffsetDateTime createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private OffsetDateTime lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;
}
