package com.leijendary.spring.microservicetemplate.factory;

import com.leijendary.spring.microservicetemplate.data.request.SampleRequest;
import com.leijendary.spring.microservicetemplate.data.response.SampleResponse;
import com.leijendary.spring.microservicetemplate.factory.mapper.SampleRequestToSampleTablePropertyMap;
import com.leijendary.spring.microservicetemplate.model.SampleTable;
import lombok.Getter;
import org.modelmapper.ModelMapper;

public class SampleFactory {

    private static SampleFactory instance = null;

    @Getter
    private final ModelMapper mapper;

    private SampleFactory() {
        final var mapper = new ModelMapper();
        mapper.addMappings(new SampleRequestToSampleTablePropertyMap());

        this.mapper = mapper;
    }

    public static SampleFactory getInstance() {
        if (instance == null) {
            instance = new SampleFactory();
        }

        return instance;
    }

    public static SampleResponse toResponse(SampleTable sampleTable) {
        return getInstance().getMapper().map(sampleTable, SampleResponse.class);
    }

    public static SampleTable of(SampleRequest sampleRequest) {
        return getInstance().getMapper().map(sampleRequest, SampleTable.class);
    }

    public static void map(SampleRequest sampleRequest, SampleTable sampleTable) {
        getInstance().getMapper().map(sampleRequest, sampleTable);
    }
}
