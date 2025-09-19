package com.leijendary.domain.image

import com.leijendary.domain.image.Image.Companion.ENTITY
import com.leijendary.domain.image.Image.Companion.POINTER_NAME
import com.leijendary.error.exception.ResourceNotFoundException
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

private const val QUERY_SET_VALIDATED = """
UPDATE image
SET
    media_type = :mediaType,
    validated = true
WHERE name = :name
RETURNING id
"""

@Transactional(readOnly = true)
interface ImageRepository : CrudRepository<Image, String> {
    fun findByName(name: String): Optional<Image>

    @Transactional
    @Modifying
    fun deleteByName(name: String)

    @Query(QUERY_SET_VALIDATED)
    fun setValidated(name: String, mediaType: String): String
}

@Transactional(readOnly = true)
fun ImageRepository.findByNameOrThrow(name: String): Image {
    return findByName(name).orElseThrow { ResourceNotFoundException(name, ENTITY, POINTER_NAME) }
}
