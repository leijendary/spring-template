package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class IdentityIdModel extends AbstractModel {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
}
