package com.leijendary.spring.microservicetemplate.document;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Data
public abstract class LocaleDocument {

    @Field(type = Text)
    private String language;

    @Field(type = FieldType.Integer)
    private int ordinal;
}
