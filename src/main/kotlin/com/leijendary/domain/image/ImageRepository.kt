package com.leijendary.domain.image

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(readOnly = true)
interface ImageRepository : CrudRepository<Image, Long> {
    fun findByName(name: String): Optional<Image>

    @Transactional
    @Query("update image set media_type = :mediaType, validated = true where name = :name returning id")
    fun setValidated(name: String, mediaType: String): Long

    @Transactional
    @Modifying
    @Query("delete from image where name = :name")
    fun deleteByName(name: String)
}
