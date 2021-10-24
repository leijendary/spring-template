package com.leijendary.spring.microservicetemplate.document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.time.OffsetDateTime;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_time;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "sample")
public class SampleDocument extends LocalizedDocument<SampleTranslationDocument> {

    @Id
    @Field(type = FieldType.Long)
    private long id;

    @Field(type = Text)
    private String column1;

    @Field(type = FieldType.Integer)
    private int column2;

    @Field(type = FieldType.Date, format = date_time)
    private OffsetDateTime createdDate;
}
