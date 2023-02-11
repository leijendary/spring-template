package com.leijendary.spring.template.core.repository

import com.leijendary.spring.template.core.entity.SoftDeleteEntity
import com.leijendary.spring.template.core.util.RequestContext.now
import com.leijendary.spring.template.core.util.RequestContext.userId
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository

@Repository
class SoftDeleteRepositoryImpl<T : SoftDeleteEntity>(
    @PersistenceContext
    private val entityManager: EntityManager
) : SoftDeleteRepository<T> {

    override fun softDelete(entity: T) {
        entity.deletedAt = now
        entity.deletedBy = userId

        entityManager.persist(entity)
    }
}
