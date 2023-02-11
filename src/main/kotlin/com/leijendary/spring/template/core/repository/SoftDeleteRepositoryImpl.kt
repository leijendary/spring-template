package com.leijendary.spring.template.core.repository

import com.leijendary.spring.template.core.entity.SoftDeleteEntity
import com.leijendary.spring.template.core.util.RequestContext.now
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Repository

@Repository
class SoftDeleteRepositoryImpl<T : SoftDeleteEntity>(
    private val auditorAware: AuditorAware<String>,

    @PersistenceContext
    private val entityManager: EntityManager
) : SoftDeleteRepository<T> {

    override fun softDelete(entity: T) {
        entity.deletedAt = now
        entity.deletedBy = auditorAware.currentAuditor.get()

        entityManager.persist(entity)
    }
}
