package com.leijendary.spring.microservicetemplate.factory.mapper;

import com.leijendary.spring.microservicetemplate.data.request.v1.SampleRequestV1;
import com.leijendary.spring.microservicetemplate.model.SampleTable;
import org.modelmapper.PropertyMap;

public class SampleRequestV1ToSampleTablePropertyMap extends PropertyMap<SampleRequestV1, SampleTable> {

    @Override
    protected void configure() {
        map().setColumn1(source.getField1());
        map().setColumn2(source.getField2());
    }
}
