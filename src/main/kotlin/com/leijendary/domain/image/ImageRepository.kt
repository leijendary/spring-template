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
    @Query("update image set media_type = :mediaType, validated = true where name = :name returning id")
    fun setValidated(name: String, mediaType: String): String

    @Transactional
    @Modifying
    @Query("delete from image where name = :name")
    fun deleteByName(name: String)
}

@Transactional(readOnly = true)
fun ImageRepository.findByNameOrThrow(name: String): Image {
    return findByName(name).orElseThrow { ResourceNotFoundException(name, ENTITY, ERROR_SOURCE_NAME) }
}
