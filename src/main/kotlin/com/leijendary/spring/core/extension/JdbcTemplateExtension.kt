package com.leijendary.spring.core.extension

import com.leijendary.spring.core.exception.VersionConflictException
import com.leijendary.spring.core.model.Seek
import com.leijendary.spring.core.model.SeekNextToken
import com.leijendary.spring.core.model.SeekRequest
import com.leijendary.spring.core.projection.SeekProjection
import com.leijendary.spring.core.util.BeanContainer.encryption
import com.leijendary.spring.core.util.RequestContext.userIdOrSystem
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.util.Base64.getDecoder
import java.util.Base64.getEncoder

private const val SEEK_OFFSET = 1
private const val SQL_SEEK_WHERE = "%s (created_at, id) %s (?, ?)"
private const val SQL_SEEK_ORDER = "order by created_at %s, id %s"
private const val SQL_SEEK_LIMIT = "limit %d"
private const val SQL_SOFT_DELETE = "update %s set deleted_at = now(), deleted_by = ? where id = ? and version = ?"
private val ENCODER = getEncoder()
private val DECODER = getDecoder()

fun <T : SeekProjection> JdbcTemplate.seek(
    sql: String,
    seekRequest: SeekRequest,
    rowMapper: RowMapper<T>,
    vararg args: Any,
    conditioned: Boolean = false
): Seek<T> {
    val (where, values) = buildWhere(seekRequest, conditioned)
    val orderBy = SQL_SEEK_ORDER.format(seekRequest.direction, seekRequest.direction)
    val limit = SQL_SEEK_LIMIT.format(seekRequest.limit + SEEK_OFFSET)
    var result = query("$sql $where $orderBy $limit", rowMapper, *args, *values)
    var size = result.size
    var nextToken = seekRequest.nextToken
    var isLast = true

    if (size > seekRequest.limit) {
        result = result.dropLast(SEEK_OFFSET)
        size -= SEEK_OFFSET
        val last = result.last()
        val seekToken = SeekNextToken(last.createdAt, last.id)
        nextToken = encode(seekToken)
        isLast = false
    }

    return Seek(result, nextToken, seekRequest.limit, size, isLast)
}

fun JdbcTemplate.softDelete(entity: String, table: String, id: Any, version: Int): Int {
    val sql = SQL_SOFT_DELETE.format(table)
    val affected = update(sql, userIdOrSystem, id, version)

    if (affected == 0) {
        throw VersionConflictException(id, entity)
    }

    return affected
}

private fun buildWhere(seekRequest: SeekRequest, conditioned: Boolean): Pair<String, Array<Any>> {
    val seekNextToken = seekRequest.nextToken?.let(::decode) ?: return "" to emptyArray()
    val starting = if (conditioned) "and" else "where"
    val operation = if (seekRequest.direction.isAscending) ">" else "<"
    val where = SQL_SEEK_WHERE.format(starting, operation)
    val values = arrayOf<Any>(seekNextToken.createdAt, seekNextToken.id)

    return where to values
}

private fun encode(seekNextToken: SeekNextToken): String {
    val json = seekNextToken.toJson()
    val encrypted = encryption.encrypt(json)
    val bytes = encrypted.encodeToByteArray()

    return ENCODER.encodeToString(bytes)
}

private fun decode(value: String): SeekNextToken {
    val decoded = DECODER.decode(value)
    val string = decoded.decodeToString()
    val json = encryption.decrypt(string)

    return json.toClass()
}
