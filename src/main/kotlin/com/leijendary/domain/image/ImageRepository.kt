package com.leijendary.domain.image

import com.leijendary.domain.image.Image.Companion.ENTITY
import com.leijendary.domain.image.Image.Companion.ERROR_SOURCE_NAME
import com.leijendary.error.exception.ResourceNotFoundException
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(readOnly = true)
interface ImageRepository : CrudRepository<Image, String> {
    fun findByName(name: String): Optional<Image>

    @Transactional
    @Query("UPDATE image SET media_type = :mediaType, validated = true WHERE name = :name RETURNING id")
    fun setValidated(name: String, mediaType: String): String

    @Transactional
    @Modifying
    @Query("DELETE FROM image WHERE name = :name")
    fun deleteByName(name: String)
}

@Transactional(readOnly = true)
fun ImageRepository.findByNameOrThrow(name: String): Image {
    return findByName(name).orElseThrow { ResourceNotFoundException(name, ENTITY, ERROR_SOURCE_NAME) }
}
