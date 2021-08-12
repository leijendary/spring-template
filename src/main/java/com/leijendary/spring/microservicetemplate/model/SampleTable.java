package com.leijendary.spring.microservicetemplate.model;

import com.leijendary.spring.microservicetemplate.model.listener.SampleTableListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@EntityListeners({ AuditingEntityListener.class, SampleTableListener.class })
public class SampleTable extends LocalizedModel<SampleTable, SampleTableTranslation> {

    @Column(name = "column_1")
    private String column1;

    @Column(name = "column_2")
    private int column2;

    @CreatedDate
    private OffsetDateTime createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private OffsetDateTime lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;
}
