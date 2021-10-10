package com.leijendary.spring.microservicetemplate.mapper;

import com.leijendary.schema.SampleSchema;
import com.leijendary.spring.microservicetemplate.data.SampleData;
import com.leijendary.spring.microservicetemplate.data.response.v1.SampleResponseV1;
import com.leijendary.spring.microservicetemplate.model.SampleTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface SampleMapper {

    SampleMapper INSTANCE = getMapper(SampleMapper.class);

    SampleResponseV1 toResponseV1(final SampleTable sampleTable);

    SampleSchema toSchema(final SampleTable sampleTable);

    SampleTable toEntity(final SampleData sampleData);

    @Mapping(target = "id", ignore = true)
    void map(final SampleData sampleData, final SampleTable sampleTable);
}
