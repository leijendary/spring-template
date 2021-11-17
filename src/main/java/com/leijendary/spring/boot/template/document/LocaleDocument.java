package com.leijendary.spring.boot.template.document;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

@Data
public abstract class LocaleDocument {

    @Field(type = Keyword)
    private String language;

    @Field(type = FieldType.Integer)
    private int ordinal;
}
