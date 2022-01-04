package com.leijendary.spring.boot.template.model;

import java.time.OffsetDateTime;

public interface SoftDeleteModel {

    void setDeletedAt(final OffsetDateTime deletedAt);

    void setDeletedBy(final String deletedBy);
}
