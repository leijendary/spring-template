package com.leijendary.spring.boot.template.core.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class IdentityIdModel extends AppModel {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
}
