package com.leijendary.spring.boot.template.core.repository

import com.leijendary.spring.boot.template.core.config.properties.AuthProperties
import com.leijendary.spring.boot.template.core.model.SoftDeleteModel
import com.leijendary.spring.boot.template.core.util.RequestContext.now
import com.leijendary.spring.boot.template.core.util.RequestContext.userId
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class SoftDeleteRepositoryImpl<T : SoftDeleteModel>(
    private val authProperties: AuthProperties,

    @PersistenceContext
    private val entityManager: EntityManager
) : SoftDeleteRepository<T> {

    override fun softDelete(entity: T) {
        entity.deletedAt = now
        entity.deletedBy = userId ?: authProperties.system.principal

        entityManager.persist(entity)
    }
}