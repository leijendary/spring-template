package com.leijendary.spring.microservicetemplate.factory;

import com.leijendary.spring.microservicetemplate.data.response.v1.SampleSearchResponseV1;
import com.leijendary.spring.microservicetemplate.document.SampleDocument;
import com.leijendary.spring.microservicetemplate.model.SampleTable;

public class SampleDocumentFactory extends AbstractFactory {

    public static SampleSearchResponseV1 toResponseV1(final SampleDocument sampleDocument) {
        final var translation = sampleDocument.getTranslation();
        final var name = translation.getName();
        final var description = translation.getDescription();
        final var serviceResponse = MAPPER.map(sampleDocument, SampleSearchResponseV1.class);
        serviceResponse.setName(name);
        serviceResponse.setDescription(description);

        return serviceResponse;
    }

    public static SampleDocument of(final SampleTable sampleTable) {
        return MAPPER.map(sampleTable, SampleDocument.class);
    }

    public static void map(final SampleTable sampleTable, final SampleDocument sampleDocument) {
        MAPPER.map(sampleTable, sampleDocument);
    }
}
