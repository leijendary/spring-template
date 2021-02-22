package com.leijendary.spring.microservicetemplate.factory.mapper;

import com.leijendary.spring.microservicetemplate.data.request.SampleRequest;
import com.leijendary.spring.microservicetemplate.model.SampleTable;
import org.modelmapper.PropertyMap;

public class SampleRequestToSampleTablePropertyMap extends PropertyMap<SampleRequest, SampleTable> {

    @Override
    protected void configure() {
        map().setColumn1(source.getField1());
        map().setColumn2(source.getField2());
    }
}
