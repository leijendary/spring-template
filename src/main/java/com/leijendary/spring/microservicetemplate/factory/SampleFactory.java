package com.leijendary.spring.microservicetemplate.factory;

import com.leijendary.spring.microservicetemplate.data.request.v1.SampleRequestV1;
import com.leijendary.spring.microservicetemplate.data.response.v1.SampleResponseV1;
import com.leijendary.spring.microservicetemplate.event.schema.SampleSchema;
import com.leijendary.spring.microservicetemplate.model.SampleTable;

public class SampleFactory extends AbstractFactory {

    public static SampleResponseV1 toResponseV1(final SampleTable sampleTable) {
        return MAPPER.map(sampleTable, SampleResponseV1.class);
    }

    public static SampleSchema toSchema(final SampleTable sampleTable) {
        return MAPPER.map(sampleTable, SampleSchema.class);
    }

    public static SampleTable of(final SampleRequestV1 sampleRequest) {
        return MAPPER.map(sampleRequest, SampleTable.class);
    }

    public static void map(final SampleRequestV1 source, final SampleTable destination) {
        MAPPER.map(source, destination);
    }
}
