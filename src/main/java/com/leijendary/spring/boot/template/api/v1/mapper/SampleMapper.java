package com.leijendary.spring.boot.template.api.v1.mapper;

import static org.mapstruct.factory.Mappers.getMapper;

import com.leijendary.spring.boot.template.api.v1.data.SampleEvent;
import com.leijendary.spring.boot.template.api.v1.data.SampleRequest;
import com.leijendary.spring.boot.template.api.v1.data.SampleResponse;
import com.leijendary.spring.boot.template.api.v1.data.SampleSearchResponse;
import com.leijendary.spring.boot.template.document.SampleDocument;
import com.leijendary.spring.boot.template.model.SampleTable;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper
public interface SampleMapper {

    SampleMapper INSTANCE = getMapper(SampleMapper.class);

    SampleResponse toResponse(final SampleTable sampleTable);

    @Mappings({
            @Mapping(source = "field1", target = "column1"),
            @Mapping(source = "field2", target = "column2")
    })
    SampleTable toEntity(final SampleRequest sampleRequestV1);

    @Mappings({
            @Mapping(source = "translation.name", target = "name"),
            @Mapping(source = "translation.description", target = "description")
    })
    SampleSearchResponse toSearchResponse(final SampleDocument sampleDocument);

    SampleDocument toDocument(final SampleTable sampleTable);

    void update(final SampleRequest sampleRequest, @MappingTarget final SampleTable sampleTable);

    void update(final SampleTable sampleTable, @MappingTarget final SampleDocument sampleDocument);

    SampleEvent toEvent(final SampleTable sampleTable);
}
