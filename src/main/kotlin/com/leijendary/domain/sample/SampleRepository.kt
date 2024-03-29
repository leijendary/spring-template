package com.leijendary.domain.sample

import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.error.exception.VersionConflictException
import com.leijendary.model.ErrorSource
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import com.leijendary.model.SeekRequest
import com.leijendary.util.embedded
import com.leijendary.util.language
import com.leijendary.util.userIdOrSystem
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Stream

private const val ENTITY = "sample"
private val SOURCE = ErrorSource(pointer = "/data/$ENTITY/id")
private val SQL_PAGE = embedded("db/sql/sample/page.sql")
private val SQL_COUNT = embedded("db/sql/sample/count.sql")
private val SQL_SEEK = embedded("db/sql/sample/seek.sql")
private val SQL_CREATE = embedded("db/sql/sample/create.sql")
private val SQL_GET = embedded("db/sql/sample/get.sql")
private val SQL_UPDATE = embedded("db/sql/sample/update.sql")
private val SQL_DELETE = embedded("db/sql/sample/delete.sql")
private val SQL_STREAM = embedded("db/sql/sample/stream.sql")
private val SQL_TRANSLATIONS_LIST = embedded("db/sql/sample/translations.list.sql")
private val SQL_TRANSLATIONS_CREATE = embedded("db/sql/sample/translations.create.sql")
private val SQL_TRANSLATIONS_UPSERT = embedded("db/sql/sample/translations.upsert.sql")
private val SQL_TRANSLATIONS_DELETE = embedded("db/sql/sample/translations.delete.sql")

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
            .param("language", language)
            .param("createdAt", seekRequest.createdAt)
            .param("limit", seekRequest.limit())
            .query(SampleList::class.java)
            .list()
    }

    fun create(request: SampleRequest, userId: String = userIdOrSystem): SampleDetail {
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
        val binds = translationsBinds(id, translations)

        return jdbcClient.sql(SQL_TRANSLATIONS_CREATE)
            .param("ids", binds.ids.toTypedArray())
            .param("names", binds.names.toTypedArray())
            .param("descriptions", binds.descriptions.toTypedArray())
            .param("languages", binds.languages.toTypedArray())
            .param("ordinals", binds.ordinals.toTypedArray())
            .query(SampleTranslation::class.java)
            .list()
    }

    @Transactional(readOnly = true)
    fun get(id: Long, translate: Boolean): SampleDetail {
        return jdbcClient.sql(SQL_GET)
            .param("id", id)
            .param("language", language)
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

    fun update(id: Long, version: Int, request: SampleRequest, userId: String = userIdOrSystem): SampleDetail {
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
        val binds = translationsBinds(id, translations)

        jdbcClient.sql(SQL_TRANSLATIONS_DELETE)
            .param("id", id)
            .param("languages", binds.languages.toTypedArray())
            .update()

        return jdbcClient.sql(SQL_TRANSLATIONS_UPSERT)
            .param("ids", binds.ids.toTypedArray())
            .param("names", binds.names.toTypedArray())
            .param("descriptions", binds.descriptions.toTypedArray())
            .param("languages", binds.languages.toTypedArray())
            .param("ordinals", binds.ordinals.toTypedArray())
            .query(SampleTranslation::class.java)
            .list()
    }

    fun delete(id: Long, version: Int, userId: String = userIdOrSystem) {
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
