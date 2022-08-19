package com.leijendary.spring.template.core.repository

import com.leijendary.spring.template.core.data.ROW_ID_FIELD
import com.leijendary.spring.template.core.data.Seek
import com.leijendary.spring.template.core.data.SeekToken
import com.leijendary.spring.template.core.data.Seekable
import com.leijendary.spring.template.core.extension.AnyUtil.toJson
import com.leijendary.spring.template.core.extension.logger
import com.leijendary.spring.template.core.extension.toClass
import com.leijendary.spring.template.core.model.UUIDModel
import com.leijendary.spring.template.core.security.Encryption
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.query.QueryUtils.toOrders
import org.springframework.stereotype.Repository
import java.util.Base64.getDecoder
import java.util.Base64.getEncoder
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import kotlin.reflect.KClass

private const val FUNCTION_NAME = "ROW"
private const val PAGE_OFFSET = 1
private val type = String::class.java
private val encoder = getEncoder()
private val decoder = getDecoder()

@Repository
class SeekPaginationRepositoryImpl<T : UUIDModel>(
    private val entityManager: EntityManager,
    private val encryption: Encryption
) : SeekPaginationRepository<T> {
    private val log = logger()
    private val criteriaBuilder: CriteriaBuilder = entityManager.criteriaBuilder

    override fun findAll(entity: KClass<T>, seekable: Seekable): Seek<T> {
        return findAll(entity, null, seekable)
    }

    override fun findAll(entity: KClass<T>, specification: Specification<T>?, seekable: Seekable): Seek<T> {
        val criteriaQuery = criteriaBuilder.createQuery(entity.javaObjectType)
        val root = criteriaQuery.from(entity.javaObjectType)
        val sort = buildSort(seekable)

        criteriaQuery.select(root)

        if (specification != null) {
            val where = buildPredicate(specification, seekable).toPredicate(root, criteriaQuery, criteriaBuilder)

            criteriaQuery.where(where)
        }

        val orderBy = toOrders(sort, root, criteriaBuilder)

        criteriaQuery.orderBy(orderBy)

        val limit = seekable.limit
        val typedQuery = entityManager.createQuery(criteriaQuery)
        typedQuery.maxResults = limit + PAGE_OFFSET

        var list = typedQuery.resultList as List<T>
        var size = list.size
        var nextToken: String? = null

        if (size > limit) {
            list = list.dropLast(PAGE_OFFSET)
            size -= PAGE_OFFSET

            val last = list.last()
            val seekToken = SeekToken(last, sort)

            nextToken = encode(seekToken)
        }

        return Seek(list, nextToken, size, seekable)
    }

    private fun buildPredicate(specification: Specification<T>, seekable: Seekable): Specification<T> {
        if (seekable.nextToken == null) {
            return specification
        }

        val token = decode(seekable.nextToken)
        val fields = token.fields
        val leftValues = ArrayList<Expression<Any>>()
        val rightValues = ArrayList<Expression<out Any>>()
        val isAscending = seekable.direction.isAscending

        return specification.and { root, _, criteriaBuilder ->
            fields.forEach {
                val path = root.get<Any>(it.key)
                val type = path.javaType
                // Convert value to it's actual data type to support JSON
                // serialization/deserialization on fields like LocalDateTime
                val value = it.value?.let { v -> criteriaBuilder.literal(v) } ?: criteriaBuilder.nullLiteral(type)

                leftValues.add(path)
                rightValues.add(value)
            }

            val leftFunction = criteriaBuilder.function(FUNCTION_NAME, type, *leftValues.toTypedArray())
            val rightFunction = criteriaBuilder.function(FUNCTION_NAME, type, *rightValues.toTypedArray())

            if (isAscending) {
                criteriaBuilder.greaterThan(leftFunction, rightFunction)
            } else {
                criteriaBuilder.lessThan(leftFunction, rightFunction)
            }
        }
    }

    private fun buildSort(seekable: Seekable): Sort {
        val orders = seekable.sort.filterNot { it.property == ROW_ID_FIELD }
        val sort = Sort.by(orders)
        val fieldSort = Sort.by(seekable.direction, ROW_ID_FIELD)

        return sort.and(fieldSort)
    }

    private fun encode(seekToken: SeekToken): String {
        val json = seekToken.toJson()!!

        log.debug("Encoding next token {}", json)

        val encrypted = encryption.encrypt(json)
        val bytes = encrypted.encodeToByteArray()

        return encoder.encodeToString(bytes)
    }

    private fun decode(value: String): SeekToken {
        val decoded = decoder.decode(value)
        val string = decoded.decodeToString()
        val json = encryption.decrypt(string)

        log.debug("Decoded next token {}", json)

        return json.toClass(SeekToken::class)
    }
}