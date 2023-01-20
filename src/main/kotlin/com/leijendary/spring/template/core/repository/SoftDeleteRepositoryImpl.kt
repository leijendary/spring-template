package com.leijendary.spring.template.core.repository

import com.leijendary.spring.template.core.model.SoftDeleteModel
import com.leijendary.spring.template.core.util.RequestContext.now
import com.leijendary.spring.template.core.util.RequestContext.userId
import org.springframework.stereotype.Repository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext

@Repository
class SoftDeleteRepositoryImpl<T : SoftDeleteModel>(
    @PersistenceContext
    private val entityManager: EntityManager
) : SoftDeleteRepository<T> {

    override fun softDelete(entity: T) {
        entity.deletedAt = now
        entity.deletedBy = userId

        entityManager.persist(entity)
    }
}