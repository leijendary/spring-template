package com.leijendary.domain.sample

import com.leijendary.domain.image.ImageResponse
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.error.exception.VersionConflictException
import com.leijendary.extension.content
import com.leijendary.model.ErrorSource
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import com.leijendary.model.SeekRequest
import com.leijendary.context.requestContext
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Stream
import kotlin.jvm.optionals.getOrNull

interface SampleRepository {
    fun page(queryRequest: QueryRequest, pageRequest: PageRequest): List<SampleList>
    fun count(queryRequest: QueryRequest): Long
    fun seek(queryRequest: QueryRequest, seekRequest: SeekRequest): MutableList<SampleList>
    fun create(request: SampleRequest, userId: String): SampleDetail
    fun get(id: Long, translate: Boolean): SampleDetail
    fun exists(id: Long): Boolean
    fun update(id: Long, version: Int, request: SampleRequest, userId: String): SampleDetail
    fun delete(id: Long, version: Int, userId: String)
    fun streamAll(): Stream<SampleDetail>
    fun listTranslations(id: Long): List<SampleTranslation>
    fun createTranslations(id: Long, translations: List<SampleTranslationRequest>): List<SampleTranslation>
    fun updateTranslations(id: Long, translations: List<SampleTranslationRequest>): List<SampleTranslation>
    fun upsertImage(id: Long, originalId: Long, previewId: Long, thumbnailId: Long)
    fun getImage(id: Long): ImageResponse?
    fun deleteImage(id: Long)
}

const val ENTITY = "sample"
val SOURCE = ErrorSource(pointer = "/data/$ENTITY/id")

private val SQL_PAGE = "db/sql/sample/page.sql".content()
private val SQL_COUNT = "db/sql/sample/count.sql".content()
private val SQL_SEEK = "db/sql/sample/seek.sql".content()
private val SQL_CREATE = "db/sql/sample/create.sql".content()
private val SQL_GET = "db/sql/sample/get.sql".content()
private val SQL_EXISTS = "db/sql/sample/exists.sql".content()
private val SQL_UPDATE = "db/sql/sample/update.sql".content()
private val SQL_DELETE = "db/sql/sample/delete.sql".content()
private val SQL_STREAM = "db/sql/sample/stream.sql".content()
private val SQL_TRANSLATIONS_LIST = "db/sql/sample/translations.list.sql".content()
private val SQL_TRANSLATIONS_CREATE = "db/sql/sample/translations.create.sql".content()
private val SQL_TRANSLATIONS_UPSERT = "db/sql/sample/translations.upsert.sql".content()
private val SQL_TRANSLATIONS_DELETE_NOT = "db/sql/sample/translations-not.delete.sql".content()
private val SQL_IMAGE_UPSERT = "db/sql/sample/image.upsert.sql".content()
private val SQL_IMAGE_GET = "db/sql/sample/image.get.sql".content()
private val SQL_IMAGE_DELETE = "db/sql/sample/image.delete.sql".content()

@Repository
class SampleRepositoryImpl(private val jdbcClient: JdbcClient) : SampleRepository {
    @Transactional(readOnly = true)
    override fun page(queryRequest: QueryRequest, pageRequest: PageRequest): List<SampleList> {
        return jdbcClient.sql(SQL_PAGE)
            .param("query", queryRequest.query)
            .param("limit", pageRequest.limit)
            .param("offset", pageRequest.offset)
            .query(SampleList::class.java)
            .list()
    }

    @Transactional(readOnly = true)
    override fun count(queryRequest: QueryRequest): Long {
        return jdbcClient.sql(SQL_COUNT).param("query", queryRequest.query).query(Long::class.java).single()
    }

    @Transactional(readOnly = true)
    override fun seek(queryRequest: QueryRequest, seekRequest: SeekRequest): MutableList<SampleList> {
        return jdbcClient.sql(SQL_SEEK)
            .param("id", seekRequest.id)
            .param("query", queryRequest.query)
            .param("language", requestContext.language)
            .param("createdAt", seekRequest.createdAt)
            .param("limit", seekRequest.limit)
            .query(SampleList::class.java)
            .list()
    }

    override fun create(request: SampleRequest, userId: String): SampleDetail {
        return jdbcClient.sql(SQL_CREATE)
            .param("name", request.name)
            .param("description", request.description)
            .param("amount", request.amount)
            .param("createdBy", userId)
            .param("lastModifiedBy", userId)
            .query(SampleDetail::class.java)
            .single()
    }

    @Transactional(readOnly = true)
    override fun get(id: Long, translate: Boolean): SampleDetail {
        return jdbcClient.sql(SQL_GET)
            .param("id", id)
            .param("language", requestContext.language)
            .param("translate", translate)
            .query(SampleDetail::class.java)
            .optional()
            .orElseThrow { ResourceNotFoundException(id, ENTITY, SOURCE) }
    }

    override fun exists(id: Long): Boolean {
        return jdbcClient.sql(SQL_EXISTS).param("id", id).query(Boolean::class.java).single()
    }

    override fun update(id: Long, version: Int, request: SampleRequest, userId: String): SampleDetail {
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

    override fun delete(id: Long, version: Int, userId: String) {
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
    override fun streamAll(): Stream<SampleDetail> {
        return jdbcClient.sql(SQL_STREAM).query(SampleDetail::class.java).stream()
    }

    @Transactional(readOnly = true)
    override fun listTranslations(id: Long): List<SampleTranslation> {
        return jdbcClient.sql(SQL_TRANSLATIONS_LIST)
            .param("id", id)
            .query(SampleTranslation::class.java)
            .list()
    }

    override fun createTranslations(id: Long, translations: List<SampleTranslationRequest>): List<SampleTranslation> {
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

    @Transactional
    override fun updateTranslations(id: Long, translations: List<SampleTranslationRequest>): List<SampleTranslation> {
        val binds = translations.toBinds()

        jdbcClient.sql(SQL_TRANSLATIONS_DELETE_NOT).param("id", id).param("languages", binds.languages).update()

        return jdbcClient.sql(SQL_TRANSLATIONS_UPSERT)
            .param("id", id)
            .param("names", binds.names)
            .param("descriptions", binds.descriptions)
            .param("languages", binds.languages)
            .param("ordinals", binds.ordinals)
            .query(SampleTranslation::class.java)
            .list()
    }

    override fun upsertImage(id: Long, originalId: Long, previewId: Long, thumbnailId: Long) {
        jdbcClient.sql(SQL_IMAGE_UPSERT)
            .param("id", id)
            .param("originalId", originalId)
            .param("previewId", previewId)
            .param("thumbnailId", thumbnailId)
            .update()
    }

    override fun getImage(id: Long): ImageResponse? {
        return jdbcClient.sql(SQL_IMAGE_GET)
            .param("id", id)
            .query(ImageResponse::class.java)
            .optional()
            .getOrNull()
    }

    override fun deleteImage(id: Long) {
        jdbcClient.sql(SQL_IMAGE_DELETE).param("id", id).update()
    }
}
