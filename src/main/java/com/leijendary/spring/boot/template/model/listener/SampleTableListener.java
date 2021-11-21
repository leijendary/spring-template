package com.leijendary.spring.boot.template.model.listener;

import static com.leijendary.spring.boot.template.util.SpringContext.getBean;
import static com.leijendary.spring.boot.template.util.TransactionUtil.afterCommit;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import com.leijendary.spring.boot.template.api.v1.mapper.SampleMapper;
import com.leijendary.spring.boot.template.event.producer.SampleProducer;
import com.leijendary.spring.boot.template.model.SampleTable;

import org.springframework.stereotype.Component;

@Component
public class SampleTableListener {

    private static final SampleMapper MAPPER = SampleMapper.INSTANCE;

    @PostPersist
    public void onSave(final SampleTable sampleTable) {
        afterCommit(() -> {
            final var sampleProducer = getBean(SampleProducer.class);
            final var sampleEvent = MAPPER.toEvent(sampleTable);

            sampleProducer.create(sampleEvent);
        });
    }

    @PostUpdate
    public void onUpdate(final SampleTable sampleTable) {
        afterCommit(() -> {
            final var sampleProducer = getBean(SampleProducer.class);
            final var sampleEvent = MAPPER.toEvent(sampleTable);

            sampleProducer.update(sampleEvent);
        });
    }

    @PostRemove
    public void onDelete(final SampleTable sampleTable) {
        afterCommit(() -> {
            final var sampleProducer = getBean(SampleProducer.class);
            final var sampleEvent = MAPPER.toEvent(sampleTable);

            sampleProducer.delete(sampleEvent);
        });
    }
}
