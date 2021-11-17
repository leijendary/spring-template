package com.leijendary.spring.boot.template.model.listener;

import static com.leijendary.spring.boot.template.util.SpringContext.getBean;
import static com.leijendary.spring.boot.template.util.TransactionUtil.afterCommit;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import com.leijendary.spring.boot.template.event.producer.SampleProducer;
import com.leijendary.spring.boot.template.mapper.SampleMapper;
import com.leijendary.spring.boot.template.model.SampleTable;

import org.springframework.stereotype.Component;

@Component
public class SampleTableListener {

    private static final SampleMapper MAPPER = SampleMapper.INSTANCE;

    @PostPersist
    public void onSave(final SampleTable sampleTable) {
        afterCommit(() -> {
            final var sampleProducer = getBean(SampleProducer.class);
            final var sampleData = MAPPER.toData(sampleTable);

            sampleProducer.create(sampleData);
        });
    }

    @PostUpdate
    public void onUpdate(final SampleTable sampleTable) {
        afterCommit(() -> {
            final var sampleProducer = getBean(SampleProducer.class);
            final var sampleData = MAPPER.toData(sampleTable);

            sampleProducer.update(sampleData);
        });
    }

    @PostRemove
    public void onDelete(final SampleTable sampleTable) {
        afterCommit(() -> {
            final var sampleProducer = getBean(SampleProducer.class);
            final var sampleData = MAPPER.toData(sampleTable);

            sampleProducer.delete(sampleData);
        });
    }
}
