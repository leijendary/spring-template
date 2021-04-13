package com.leijendary.spring.microservicetemplate.model.listener;

import com.leijendary.spring.microservicetemplate.event.producer.SampleProducer;
import com.leijendary.spring.microservicetemplate.factory.SampleFactory;
import com.leijendary.spring.microservicetemplate.model.SampleTable;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.inject.Inject;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class SampleTableListener {

    @Inject
    private SampleProducer sampleProducer;

    public SampleTableListener() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @PostPersist
    public void onSave(final SampleTable sampleTable) {
        final var sampleSchema = SampleFactory.toSchema(sampleTable);

        sampleProducer.created(sampleSchema);
    }

    @PostUpdate
    public void onUpdate(final SampleTable sampleTable) {
        final var sampleSchema = SampleFactory.toSchema(sampleTable);

        sampleProducer.updated(sampleSchema);
    }

    @PostRemove
    public void onDelete(final SampleTable sampleTable) {
        final var sampleSchema = SampleFactory.toSchema(sampleTable);

        sampleProducer.deleted(sampleSchema);
    }
}
