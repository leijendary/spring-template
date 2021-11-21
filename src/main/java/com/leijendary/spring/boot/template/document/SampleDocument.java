package com.leijendary.spring.boot.template.document;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_time;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "sample")
@Setting(settingPath = "/elasticsearch/ngram-analyzer.settings.json")
public class SampleDocument extends LocalizedDocument<SampleTranslationDocument> {

    @Id
    @Field(type = FieldType.Keyword)
    private UUID id;

    @Field(type = Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    private String column1;

    @Field(type = FieldType.Integer)
    private int column2;

    @Field(type = FieldType.Date, format = date_time)
    private OffsetDateTime createdAt;
}
