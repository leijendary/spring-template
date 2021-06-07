package com.leijendary.spring.microservicetemplate.model;

import com.leijendary.spring.microservicetemplate.model.listener.SampleTableListener;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@EntityListeners(SampleTableListener.class)
public class SampleTable extends AbstractModel {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "column_1")
    private String column1;

    @Column(name = "column_2")
    private int column2;
}
