package com.leijendary.spring.microservicetemplate.model.listener;

import com.leijendary.spring.microservicetemplate.event.producer.SampleProducer;
import com.leijendary.spring.microservicetemplate.model.SampleTable;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import static com.leijendary.spring.microservicetemplate.factory.SampleFactory.toSchema;
import static com.leijendary.spring.microservicetemplate.util.SpringContext.getBean;

public class SampleTableListener {

    @PostPersist
    public void onSave(final SampleTable sampleTable) {
        final var sampleProducer = getBean(SampleProducer.class);
        final var sampleSchema = toSchema(sampleTable);

        sampleProducer.created(sampleSchema);
    }

    @PostUpdate
    public void onUpdate(final SampleTable sampleTable) {
        final var sampleProducer = getBean(SampleProducer.class);
        final var sampleSchema = toSchema(sampleTable);

        sampleProducer.updated(sampleSchema);
    }

    @PostRemove
    public void onDelete(final SampleTable sampleTable) {
        final var sampleProducer = getBean(SampleProducer.class);
        final var sampleSchema = toSchema(sampleTable);

        sampleProducer.deleted(sampleSchema);
    }
}
