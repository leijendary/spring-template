package com.leijendary.spring.microservicetemplate.model.listener;

import com.leijendary.spring.microservicetemplate.event.producer.SampleProducer;
import com.leijendary.spring.microservicetemplate.model.SampleTable;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import static com.leijendary.spring.microservicetemplate.factory.SampleFactory.toSchema;
import static com.leijendary.spring.microservicetemplate.util.SpringContext.getBean;
import static com.leijendary.spring.microservicetemplate.util.TransactionUtil.afterCommit;

@Slf4j
public class SampleTableListener {

    @PostPersist
    public void onSave(final SampleTable sampleTable) {
        afterCommit(() -> {
            final var sampleProducer = getBean(SampleProducer.class);
            final var sampleSchema = toSchema(sampleTable);

            sampleProducer.create(sampleSchema);
        });
    }

    @PostUpdate
    public void onUpdate(final SampleTable sampleTable) {
        afterCommit(() -> {
            final var sampleProducer = getBean(SampleProducer.class);
            final var sampleSchema = toSchema(sampleTable);

            sampleProducer.update(sampleSchema);
        });
    }

    @PostRemove
    public void onDelete(final SampleTable sampleTable) {
        afterCommit(() -> {
            final var sampleProducer = getBean(SampleProducer.class);
            final var sampleSchema = toSchema(sampleTable);

            sampleProducer.delete(sampleSchema);
        });
    }
}
