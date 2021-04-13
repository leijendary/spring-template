package com.leijendary.spring.microservicetemplate.model;

import com.leijendary.spring.microservicetemplate.model.listener.SampleTableListener;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@EntityListeners(SampleTableListener.class)
public class SampleTable extends AppModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "column_1")
    private String column1;

    @Column(name = "column_2")
    private int column2;
}
