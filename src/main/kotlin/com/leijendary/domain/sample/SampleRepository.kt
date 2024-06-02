package com.leijendary.domain.sample

import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.error.exception.VersionConflictException
import com.leijendary.extension.content
import com.leijendary.model.ErrorSource
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import com.leijendary.model.SeekRequest
import com.leijendary.util.requestContext
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Stream

private const val ENTITY = "sample"
private val SOURCE = ErrorSource(pointer = "/data/$ENTITY/id")
private val SQL_PAGE = "db/sql/sample/page.sql".content()
private val SQL_COUNT = "db/sql/sample/count.sql".content()
private val SQL_SEEK = "db/sql/sample/seek.sql".content()
private val SQL_CREATE = "db/sql/sample/create.sql".content()
private val SQL_GET = "db/sql/sample/get.sql".content()
private val SQL_UPDATE = "db/sql/sample/update.sql".content()
private val SQL_DELETE = "db/sql/sample/delete.sql".content()
private val SQL_STREAM = "db/sql/sample/stream.sql".content()
private val SQL_TRANSLATIONS_LIST = "db/sql/sample/translations.list.sql".content()
private val SQL_TRANSLATIONS_CREATE = "db/sql/sample/translations.create.sql".content()
private val SQL_TRANSLATIONS_UPSERT = "db/sql/sample/translations.upsert.sql".content()
private val SQL_TRANSLATIONS_DELETE_NOT = "db/sql/sample/translations-not.delete.sql".content()

@Repository
class SampleRepository(private val jdbcClient: JdbcClient) {
    @Transactional(readOnly = true)
    fun page(queryRequest: QueryRequest, pageRequest: PageRequest): List<SampleList> {
        return jdbcClient.sql(SQL_PAGE)
            .param("query", queryRequest.query)
            .param("limit", pageRequest.limit())
            .param("offset", pageRequest.offset())
            .query(SampleList::class.java)
            .list()
    }

    @Transactional(readOnly = true)
    fun count(queryRequest: QueryRequest): Long {
        return jdbcClient.sql(SQL_COUNT)
            .param("query", queryRequest.query)
            .query(Long::class.java)
            .single()
    }

    @Transactional(readOnly = true)
    fun seek(queryRequest: QueryRequest, seekRequest: SeekRequest): MutableList<SampleList> {
        return jdbcClient.sql(SQL_SEEK)
            .param("id", seekRequest.id)
            .param("query", queryRequest.query)
            .param("language", requestContext.language)
            .param("createdAt", seekRequest.createdAt)
            .param("limit", seekRequest.limit())
            .query(SampleList::class.java)
            .list()
    }

    fun create(request: SampleRequest, userId: String = requestContext.userIdOrSystem): SampleDetail {
        return jdbcClient.sql(SQL_CREATE)
            .param("name", request.name)
            .param("description", request.description)
            .param("amount", request.amount)
            .param("createdBy", userId)
            .param("lastModifiedBy", userId)
            .query(SampleDetail::class.java)
            .single()
    }

    fun createTranslations(id: Long, translations: List<SampleTranslationRequest>): List<SampleTranslation> {
        val binds = translations.toBinds()

        return jdbcClient.sql(SQL_TRANSLATIONS_CREATE)
            .param("id", id)
            .param("names", binds.names)
            .param("descriptions", binds.descriptions)
            .param("languages", binds.languages)
            .param("ordinals", binds.ordinals)
            .query(SampleTranslation::class.java)
            .list()
    }

    @Transactional(readOnly = true)
    fun get(id: Long, translate: Boolean): SampleDetail {
        return jdbcClient.sql(SQL_GET)
            .param("id", id)
            .param("language", requestContext.language)
            .param("translate", translate)
            .query(SampleDetail::class.java)
            .optional()
            .orElseThrow { ResourceNotFoundException(id, ENTITY, SOURCE) }
    }

    @Transactional(readOnly = true)
    fun listTranslations(id: Long): List<SampleTranslation> {
        return jdbcClient.sql(SQL_TRANSLATIONS_LIST)
            .param("id", id)
            .query(SampleTranslation::class.java)
            .list()
    }

    fun update(
        id: Long,
        version: Int,
        request: SampleRequest,
        userId: String = requestContext.userIdOrSystem
    ): SampleDetail {
        return jdbcClient.sql(SQL_UPDATE)
            .param("id", id)
            .param("version", version)
            .param("name", request.name)
            .param("description", request.description)
            .param("amount", request.amount)
            .param("lastModifiedBy", userId)
            .query(SampleDetail::class.java)
            .optional()
            .orElseThrow { VersionConflictException(id, ENTITY, version) }
    }

    @Transactional
    fun updateTranslations(id: Long, translations: List<SampleTranslationRequest>): List<SampleTranslation> {
        val binds = translations.toBinds()

        jdbcClient.sql(SQL_TRANSLATIONS_DELETE_NOT)
            .param("id", id)
            .param("languages", binds.languages)
            .update()

        return jdbcClient.sql(SQL_TRANSLATIONS_UPSERT)
            .param("id", id)
            .param("names", binds.names)
            .param("descriptions", binds.descriptions)
            .param("languages", binds.languages)
            .param("ordinals", binds.ordinals)
            .query(SampleTranslation::class.java)
            .list()
    }

    fun delete(id: Long, version: Int, userId: String = requestContext.userIdOrSystem) {
        val count = jdbcClient.sql(SQL_DELETE)
            .param("id", id)
            .param("version", version)
            .param("deletedBy", userId)
            .update()

        if (count == 0) {
            throw VersionConflictException(id, ENTITY, version)
        }
    }

    @Transactional(readOnly = true)
    fun streamAll(): Stream<SampleDetail> {
        return jdbcClient.sql(SQL_STREAM).query(SampleDetail::class.java).stream()
    }
}
