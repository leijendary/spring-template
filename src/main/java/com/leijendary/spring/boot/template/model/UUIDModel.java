package com.leijendary.spring.boot.template.model;

import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class UUIDModel extends AppModel {

    @Id
    @GeneratedValue
    private UUID id;
}
