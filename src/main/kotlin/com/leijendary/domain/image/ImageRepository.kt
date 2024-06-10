package com.leijendary.domain.image

import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.extension.content
import com.leijendary.model.ErrorSource
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

interface ImageRepository {
    fun create(name: String, userId: String): ImageResult
    fun setValidated(name: String): Long
    fun getByName(name: String): ImageResult
    fun getIdByName(name: String): Long
    fun delete(id: Long)
    fun createMetadata(id: Long, metadata: Map<String, String>)
    fun deleteMetadata(id: Long)
}

const val ENTITY = "image"
val SOURCE = ErrorSource(pointer = "/data/$ENTITY/id")
val SOURCE_NAME = ErrorSource(pointer = "/data/$ENTITY/name")
val SOURCE_STORAGE_NAME = ErrorSource(pointer = "/storage/$ENTITY/name")

private val SQL_CREATE = "db/sql/image/create.sql".content()
private val SQL_SET_VALIDATED = "db/sql/image/set-validated.sql".content()
private val SQL_GET_BY_NAME = "db/sql/image/by-name.get.sql".content()
private val SQL_GET_ID_BY_NAME = "db/sql/image/id-by-name.get.sql".content()
private val SQL_DELETE = "db/sql/image/delete.sql".content()
private val SQL_METADATA_CREATE = "db/sql/image/metadata.create.sql".content()
private val SQL_METADATA_DELETE = "db/sql/image/metadata.delete.sql".content()

@Repository
class ImageRepositoryImpl(private val jdbcClient: JdbcClient) : ImageRepository {
    override fun create(name: String, userId: String): ImageResult {
        return jdbcClient.sql(SQL_CREATE)
            .param("name", name)
            .param("createdBy", userId)
            .query(ImageResult::class.java)
            .single()
    }

    override fun setValidated(name: String): Long {
        return jdbcClient.sql(SQL_SET_VALIDATED).param("name", name).query(Long::class.java).single()
    }

    override fun getByName(name: String): ImageResult {
        return jdbcClient.sql(SQL_GET_BY_NAME)
            .param("name", name)
            .query(ImageResult::class.java)
            .single()
    }

    override fun getIdByName(name: String): Long {
        return jdbcClient.sql(SQL_GET_ID_BY_NAME)
            .param("name", name)
            .query(Long::class.java)
            .optional()
            .orElseThrow { ResourceNotFoundException(name, ENTITY, SOURCE_NAME) }
    }

    override fun delete(id: Long) {
        val count = jdbcClient.sql(SQL_DELETE).param("id", id).update()

        if (count == 0) {
            throw ResourceNotFoundException(id, ENTITY, SOURCE)
        }
    }

    override fun createMetadata(id: Long, metadata: Map<String, String>) {
        jdbcClient.sql(SQL_METADATA_CREATE)
            .param("id", id)
            .param("names", metadata.keys.toTypedArray())
            .param("values", metadata.values.toTypedArray())
            .update()
    }

    override fun deleteMetadata(id: Long) {
        jdbcClient.sql(SQL_METADATA_DELETE).param("id", id).update()
    }
}
