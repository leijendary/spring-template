package com.leijendary.spring.microservicetemplate.model.listener;

import com.leijendary.spring.microservicetemplate.event.producer.SampleProducer;
import com.leijendary.spring.microservicetemplate.model.SampleTable;

import javax.inject.Inject;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import static com.leijendary.spring.microservicetemplate.factory.SampleFactory.toSchema;
import static org.springframework.web.context.support.SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext;

public class SampleTableListener {

    @Inject
    private SampleProducer sampleProducer;

    public SampleTableListener() {
        processInjectionBasedOnCurrentContext(this);
    }

    @PostPersist
    public void onSave(final SampleTable sampleTable) {
        final var sampleSchema = toSchema(sampleTable);

        sampleProducer.created(sampleSchema);
    }

    @PostUpdate
    public void onUpdate(final SampleTable sampleTable) {
        final var sampleSchema = toSchema(sampleTable);

        sampleProducer.updated(sampleSchema);
    }

    @PostRemove
    public void onDelete(final SampleTable sampleTable) {
        final var sampleSchema = toSchema(sampleTable);

        sampleProducer.deleted(sampleSchema);
    }
}
