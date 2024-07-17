package com.leijendary.domain.image

import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.extension.content
import com.leijendary.model.ErrorSource
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

interface ImageRepository {
    fun create(name: String, userId: String): ImageResult
    fun setValidated(name: String, mediaType: String): Long
    fun getByName(name: String): ImageResult
    fun deleteByName(name: String)
    fun createMetadata(id: Long, metadata: Map<String, String>)
}

const val ENTITY = "image"
val SOURCE_NAME = ErrorSource(pointer = "/data/$ENTITY/name")
val SOURCE_STORAGE_NAME = ErrorSource(pointer = "/storage/$ENTITY/name")

private val SQL_CREATE = "db/sql/image/create.sql".content()
private val SQL_SET_VALIDATED = "db/sql/image/set-validated.sql".content()
private val SQL_GET_BY_NAME = "db/sql/image/by-name.get.sql".content()
private val SQL_DELETE_BY_NAME = "db/sql/image/by-name.delete.sql".content()
private val SQL_METADATA_CREATE = "db/sql/image/metadata.create.sql".content()

@Repository
class ImageRepositoryImpl(private val jdbcClient: JdbcClient) : ImageRepository {
    override fun create(name: String, userId: String): ImageResult {
        return jdbcClient.sql(SQL_CREATE)
            .param("name", name)
            .param("createdBy", userId)
            .query(ImageResult::class.java)
            .single()
    }

    override fun setValidated(name: String, mediaType: String): Long {
        return jdbcClient.sql(SQL_SET_VALIDATED)
            .param("name", name)
            .param("mediaType", mediaType)
            .query(Long::class.java)
            .optional()
            .orElseThrow { ResourceNotFoundException(name, ENTITY, SOURCE_NAME) }
    }

    override fun getByName(name: String): ImageResult {
        return jdbcClient.sql(SQL_GET_BY_NAME)
            .param("name", name)
            .query(ImageResult::class.java)
            .optional()
            .orElseThrow { ResourceNotFoundException(name, ENTITY, SOURCE_NAME) }
    }

    override fun deleteByName(name: String) {
        val count = jdbcClient.sql(SQL_DELETE_BY_NAME).param("name", name).update()

        if (count == 0) {
            throw ResourceNotFoundException(name, ENTITY, SOURCE_NAME)
        }
    }

    override fun createMetadata(id: Long, metadata: Map<String, String>) {
        jdbcClient.sql(SQL_METADATA_CREATE)
            .param("id", id)
            .param("names", metadata.keys.toTypedArray())
            .param("values", metadata.values.toTypedArray())
            .update()
    }
}
