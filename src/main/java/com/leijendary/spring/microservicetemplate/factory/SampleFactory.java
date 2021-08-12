package com.leijendary.spring.microservicetemplate.factory;

import com.leijendary.schema.SampleSchema;
import com.leijendary.spring.microservicetemplate.data.SampleData;
import com.leijendary.spring.microservicetemplate.data.request.v1.SampleRequestV1;
import com.leijendary.spring.microservicetemplate.data.response.v1.SampleResponseV1;
import com.leijendary.spring.microservicetemplate.model.SampleTable;

public class SampleFactory extends AbstractFactory {

    public static SampleResponseV1 toResponseV1(final SampleTable sampleTable) {
        final var response = MAPPER.map(sampleTable, SampleResponseV1.class);

        // Get the first translation then change the fields
        sampleTable.getTranslations()
                .stream()
                .findFirst()
                .ifPresent(translation -> {
                    response.setColumn1(translation.getName());
                    response.setColumn2(translation.getDescription());
                });

        return response;
    }

    public static SampleSchema toSchema(final SampleTable sampleTable) {
        return MAPPER.map(sampleTable, SampleSchema.class);
    }

    public static SampleTable of(final SampleRequestV1 sampleRequest) {
        return MAPPER.map(sampleRequest, SampleTable.class);
    }

    public static SampleTable of(final SampleData sampleData) {
        return MAPPER.map(sampleData, SampleTable.class);
    }

    public static void map(final SampleData sampleData, final SampleTable destination) {
        MAPPER.map(sampleData, destination);
    }
}
