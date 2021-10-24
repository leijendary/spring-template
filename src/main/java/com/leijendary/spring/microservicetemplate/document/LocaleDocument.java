package com.leijendary.spring.microservicetemplate.document;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

@Data
public abstract class LocaleDocument {

    @Field(type = Keyword)
    private String language;

    @Field(type = FieldType.Integer)
    private int ordinal;
}
