package com.leijendary.domain.sample

import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.error.exception.VersionConflictException
import com.leijendary.model.ErrorSource
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import com.leijendary.model.SeekRequest
import com.leijendary.util.includeString
import com.leijendary.util.language
import com.leijendary.util.userIdOrSystem
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Stream

private const val ENTITY = "sample"
private val SOURCE = ErrorSource(pointer = "/data/$ENTITY/id")
private val SQL_PAGE = includeString("db/sql/sample/page.sql")
private val SQL_COUNT = includeString("db/sql/sample/count.sql")
private val SQL_SEEK = includeString("db/sql/sample/seek.sql")
private val SQL_CREATE = includeString("db/sql/sample/create.sql")
private val SQL_GET = includeString("db/sql/sample/get.sql")
private val SQL_UPDATE = includeString("db/sql/sample/update.sql")
private val SQL_DELETE = includeString("db/sql/sample/delete.sql")
private val SQL_STREAM = includeString("db/sql/sample/stream.sql")
private val SQL_TRANSLATIONS_LIST = includeString("db/sql/sample/translations.list.sql")
private val SQL_TRANSLATIONS_CREATE = includeString("db/sql/sample/translations.create.sql")
private val SQL_TRANSLATIONS_UPSERT = includeString("db/sql/sample/translations.upsert.sql")
private val SQL_TRANSLATIONS_DELETE = includeString("db/sql/sample/translations.delete.sql")

@Repository
class SampleRepository(private val jdbcClient: JdbcClient) {
    @Transactional(readOnly = true)
    fun page(queryRequest: QueryRequest, pageRequest: PageRequest): List<SampleList> {
        return jdbcClient.sql(SQL_PAGE)
            .params(queryRequest.query, pageRequest.limit(), pageRequest.offset())
            .query(SampleList::class.java)
            .list()
    }

    @Transactional(readOnly = true)
    fun count(queryRequest: QueryRequest): Long {
        return jdbcClient.sql(SQL_COUNT)
            .param(queryRequest.query)
            .query(Long::class.java)
            .single()
    }

    @Transactional(readOnly = true)
    fun seek(queryRequest: QueryRequest, seekRequest: SeekRequest): List<SampleList> {
        return jdbcClient.sql(SQL_SEEK)
            .param("language", language)
            .param("query", queryRequest.query)
            .param("limit", seekRequest.limit())
            .param("createdAt", seekRequest.createdAt)
            .param("id", seekRequest.id)
            .query(SampleList::class.java)
            .list()
    }

    fun create(request: SampleRequest): SampleDetail {
        return jdbcClient.sql(SQL_CREATE)
            .params(request.name, request.description, request.amount, userIdOrSystem, userIdOrSystem)
            .query(SampleDetail::class.java)
            .single()
    }

    fun createTranslations(id: Long, translations: List<SampleTranslationRequest>): List<SampleTranslation> {
        val binds = translationsBinds(id, translations)

        return jdbcClient.sql(SQL_TRANSLATIONS_CREATE)
            .params(
                binds.ids.toTypedArray(),
                binds.names.toTypedArray(),
                binds.descriptions.toTypedArray(),
                binds.languages.toTypedArray(),
                binds.ordinals.toTypedArray()
            )
            .query(SampleTranslation::class.java)
            .list()
    }

    @Transactional(readOnly = true)
    fun get(id: Long, translate: Boolean): SampleDetail {
        return jdbcClient.sql(SQL_GET)
            .params(language, translate, id)
            .query(SampleDetail::class.java)
            .optional()
            .orElseThrow { ResourceNotFoundException(id, ENTITY, SOURCE) }
    }

    @Transactional(readOnly = true)
    fun listTranslations(id: Long): List<SampleTranslation> {
        return jdbcClient.sql(SQL_TRANSLATIONS_LIST)
            .param(id)
            .query(SampleTranslation::class.java)
            .list()
    }

    fun update(id: Long, version: Int, request: SampleRequest): SampleDetail {
        return jdbcClient.sql(SQL_UPDATE)
            .params(request.name, request.description, request.amount, userIdOrSystem, id, version)
            .query(SampleDetail::class.java)
            .optional()
            .orElseThrow { VersionConflictException(id, ENTITY, version) }
    }

    fun updateTranslations(id: Long, translations: List<SampleTranslationRequest>): List<SampleTranslation> {
        val binds = translationsBinds(id, translations)

        jdbcClient.sql(SQL_TRANSLATIONS_DELETE)
            .params(id, binds.languages.toTypedArray())
            .update()

        return jdbcClient.sql(SQL_TRANSLATIONS_UPSERT)
            .params(
                binds.ids.toTypedArray(),
                binds.names.toTypedArray(),
                binds.descriptions.toTypedArray(),
                binds.languages.toTypedArray(),
                binds.ordinals.toTypedArray()
            )
            .query(SampleTranslation::class.java)
            .list()
    }

    fun delete(id: Long, version: Int) {
        val count = jdbcClient.sql(SQL_DELETE)
            .params(userIdOrSystem, id, version)
            .update()

        if (count == 0) {
            throw VersionConflictException(id, ENTITY, version)
        }
    }

    @Transactional(readOnly = true)
    fun streamAll(): Stream<SampleDetail> {
        return jdbcClient.sql(SQL_STREAM).query(SampleDetail::class.java).stream()
    }

    private fun translationsBinds(id: Long, translations: List<SampleTranslationRequest>): SampleTranslationsBinds {
        val binds = SampleTranslationsBinds(
            ids = List(translations.size) { id },
            names = ArrayList(translations.size),
            descriptions = ArrayList(translations.size),
            languages = ArrayList(translations.size),
            ordinals = ArrayList(translations.size),
        )

        translations.forEach {
            binds.names.add(it.name)
            binds.descriptions.add(it.description)
            binds.languages.add(it.language)
            binds.ordinals.add(it.ordinal)
        }

        return binds
    }
}
