package com.leijendary.spring.boot.template.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Version;

import com.leijendary.spring.boot.template.core.model.LocalizedModel;
import com.leijendary.spring.boot.template.core.model.SoftDeleteModel;

import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted_at is null")
public class SampleTable extends LocalizedModel<SampleTableTranslations> implements SoftDeleteModel {

    @Column(name = "column_1")
    private String column1;

    @Column(name = "column_2")
    private int column2;

    @Version
    private int version;

    @CreatedDate
    private OffsetDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    @LastModifiedBy
    private String lastModifiedBy;

    private OffsetDateTime deletedAt;
    private String deletedBy;
}
