package com.leijendary.spring.microservicetemplate.mapper;

import com.leijendary.spring.microservicetemplate.data.SampleData;
import com.leijendary.spring.microservicetemplate.data.request.v1.SampleRequestV1;
import com.leijendary.spring.microservicetemplate.data.response.v1.SampleResponseV1;
import com.leijendary.spring.microservicetemplate.data.response.v1.SampleSearchResponseV1;
import com.leijendary.spring.microservicetemplate.document.SampleDocument;
import com.leijendary.spring.microservicetemplate.model.SampleTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface SampleMapper {

    SampleMapper INSTANCE = getMapper(SampleMapper.class);

    SampleResponseV1 toResponseV1(final SampleTable sampleTable);

    SampleTable toEntity(final SampleData sampleData);

    @Mapping(target = "id", ignore = true)
    void update(final SampleData sampleData, @MappingTarget final SampleTable sampleTable);

    @Mappings({
            @Mapping(source = "field1", target = "column1"),
            @Mapping(source = "field2", target = "column2")
    })
    SampleData toData(final SampleRequestV1 sampleRequestV1);

    SampleData toData(final SampleTable sampleTable);

    @Mappings({
            @Mapping(source = "translation.name", target = "name"),
            @Mapping(source = "translation.description", target = "description")
    })
    SampleSearchResponseV1 toSearchResponseV1(final SampleDocument sampleDocument);

    SampleDocument toDocument(final SampleTable sampleTable);

    void update(final SampleTable sampleTable, @MappingTarget final SampleDocument sampleDocument);
}
