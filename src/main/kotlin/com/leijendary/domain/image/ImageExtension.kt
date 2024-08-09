package com.leijendary.domain.image

import com.leijendary.domain.image.Image.Companion.ENTITY
import com.leijendary.domain.image.Image.Companion.ERROR_SOURCE_NAME
import com.leijendary.error.exception.ResourceNotFoundException
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
fun ImageRepository.findByNameOrThrow(name: String): Image {
    return findByName(name).orElseThrow { ResourceNotFoundException(name, ENTITY, ERROR_SOURCE_NAME) }
}
