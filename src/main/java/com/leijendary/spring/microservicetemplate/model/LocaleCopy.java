package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@MappedSuperclass
public abstract class LocaleCopy<R> implements Serializable {

    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name = "reference_id")
    private R reference;

    private String language;
    private int ordinal;
}
