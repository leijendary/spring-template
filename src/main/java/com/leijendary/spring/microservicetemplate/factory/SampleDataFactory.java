package com.leijendary.spring.microservicetemplate.factory;

import com.leijendary.spring.microservicetemplate.data.SampleData;
import com.leijendary.spring.microservicetemplate.data.request.v1.SampleRequestV1;

public class SampleDataFactory extends AbstractFactory {

    public static SampleData of(final SampleRequestV1 sampleRequestV1) {
        return MAPPER.map(sampleRequestV1, SampleData.class);
    }
}
