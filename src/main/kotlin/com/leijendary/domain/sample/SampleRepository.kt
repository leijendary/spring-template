package com.leijendary.domain.sample

import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.error.exception.VersionConflictException
import com.leijendary.model.ErrorSource
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import com.leijendary.model.SeekRequest
import com.leijendary.util.RequestContext.language
import com.leijendary.util.RequestContext.userIdOrSystem
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Stream

private const val ENTITY = "sample"
private val SOURCE = ErrorSource(pointer = "/data/$ENTITY/id")

private const val SQL_PAGE = """
    select id, name, description, amount, created_at
    from $ENTITY
    where deleted_at is null and name ilike concat('%%', ?::text, '%%')
    order by created_at desc
    limit ?
    offset ?
"""

private const val SQL_COUNT = """
    select count(*)
    from $ENTITY
    where deleted_at is null and name ilike concat('%%', ?::text, '%%')
"""

private const val SQL_SEEK = """
    select s.id, t.name, t.description, amount, created_at
    from $ENTITY s
    left join lateral (
        select name, description
        from sample_translation
        where id = s.id
        order by (language = :language)::int desc, ordinal
        limit 1
    ) t on true
    where
        deleted_at is null
        and (s.name ilike concat('%%', :query::text, '%%') or t.name ilike concat('%%', :query::text, '%%'))
        and (:createdAt::timestamptz is null or :id::bigint is null or (created_at, id) < (:createdAt, :id))
    order by created_at desc, id desc
    limit :limit
"""

private const val SQL_CREATE = """
    insert into $ENTITY (name, description, amount, created_by, last_modified_by)
    values (?, ?, ?, ?, ?)
    returning id, name, description, amount, version, null translations, created_at
"""

private const val SQL_CREATE_TRANSLATIONS = """
    insert into sample_translation (id, name, description, language, ordinal)
    select * from unnest(?::bigint[], ?::text[], ?::text[], ?::text[],  ?::smallint[])
    returning name, description, language, ordinal
"""

private const val SQL_GET = """
    select
        s.id,
        coalesce(t.name, s.name) name,
        coalesce(t.description, s.description) description,
        amount,
        version,
        null translations,
        created_at
    from $ENTITY s
    left join lateral (
        select name, description
        from sample_translation
        where id = s.id
        order by (language = ?)::int desc, ordinal
        limit 1
    ) t on ?
    where id = ? and deleted_at is null
"""

private const val SQL_LIST_TRANSLATIONS = """
    select name, description, language, ordinal 
    from sample_translation 
    where id = ?
"""

private const val SQL_UPDATE = """
    update $ENTITY
    set
        name = ?,
        description = ?,
        amount = ?,
        version = version + 1,
        last_modified_at = now(),
        last_modified_by = ?
    where id = ? and version = ?
    returning id, name, description, amount, version, null translations, created_at
"""

private const val SQL_DELETE_TRANSLATIONS_NOT = "delete from sample_translation where id = ? and language <> all(?)"

private const val SQL_UPSERT_TRANSLATIONS = """
    insert into sample_translation (id, name, description, language, ordinal)
    select * from unnest(?::int[], ?::text[], ?::text[], ?::text[], ?::smallint[])
    on conflict (id, language)
    do update
    set
        name = excluded.name,
        description = excluded.description,
        language = excluded.language,
        ordinal = excluded.ordinal
    returning name, description, language, ordinal
"""

private const val SQL_DELETE = """
    update $ENTITY
    set version = version + 1, deleted_by = ?, deleted_at = now()
    where id = ? and version = ? and deleted_at is null
"""

private const val SQL_STREAM = """
    select id, name, description, amount, version, null translations, created_at
    from $ENTITY
    where deleted_at is null
"""

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

        return jdbcClient.sql(SQL_CREATE_TRANSLATIONS)
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
            .orElseThrow { ResourceNotFoundException(id, SOURCE) }
    }

    @Transactional(readOnly = true)
    fun listTranslations(id: Long): List<SampleTranslation> {
        return jdbcClient.sql(SQL_LIST_TRANSLATIONS)
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

        jdbcClient.sql(SQL_DELETE_TRANSLATIONS_NOT)
            .params(id, binds.languages.toTypedArray())
            .update()

        return jdbcClient.sql(SQL_UPSERT_TRANSLATIONS)
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
            binds.names.add(it.name!!)
            binds.descriptions.add(it.description)
            binds.languages.add(it.language!!)
            binds.ordinals.add(it.ordinal)
        }

        return binds
    }
}
