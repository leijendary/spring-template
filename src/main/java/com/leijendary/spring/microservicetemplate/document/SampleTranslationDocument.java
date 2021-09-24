package com.leijendary.spring.microservicetemplate.document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleTranslationDocument extends LocaleDocument {

    @Field(type = Keyword)
    private String name;

    @Field(type = Keyword)
    private String description;
}
