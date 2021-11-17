package com.leijendary.spring.boot.template.document;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

import org.springframework.data.elasticsearch.annotations.Field;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleTranslationDocument extends LocaleDocument {

    @Field(type = Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    private String name;

    @Field(type = Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    private String description;
}
