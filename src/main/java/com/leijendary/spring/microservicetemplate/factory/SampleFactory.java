package com.leijendary.spring.microservicetemplate.factory;

import com.leijendary.spring.microservicetemplate.data.request.v1.SampleRequestV1;
import com.leijendary.spring.microservicetemplate.data.response.v1.SampleResponseV1;
import com.leijendary.spring.microservicetemplate.factory.mapper.SampleRequestV1ToSampleTablePropertyMap;
import com.leijendary.spring.microservicetemplate.model.SampleTable;
import lombok.Getter;
import org.modelmapper.ModelMapper;

public class SampleFactory {

    private static SampleFactory instance = null;

    @Getter
    private final ModelMapper mapper;

    private SampleFactory() {
        final var mapper = new ModelMapper();
        mapper.addMappings(new SampleRequestV1ToSampleTablePropertyMap());

        this.mapper = mapper;
    }

    public static SampleFactory getInstance() {
        if (instance == null) {
            instance = new SampleFactory();
        }

        return instance;
    }

    public static SampleResponseV1 toResponseV1(SampleTable sampleTable) {
        return getInstance().getMapper().map(sampleTable, SampleResponseV1.class);
    }

    public static SampleTable of(SampleRequestV1 sampleRequest) {
        return getInstance().getMapper().map(sampleRequest, SampleTable.class);
    }

    public static void map(SampleRequestV1 sampleRequest, SampleTable sampleTable) {
        getInstance().getMapper().map(sampleRequest, sampleTable);
    }
}
