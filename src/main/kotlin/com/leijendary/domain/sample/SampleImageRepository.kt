package com.leijendary.domain.sample

import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(readOnly = true)
interface SampleImageRepository : CrudRepository<SampleImage, String> {
    fun <T> findById(id: String, type: Class<T>): Optional<T>

    fun <T> findByIdIn(ids: Set<String>, type: Class<T>): List<T>
}

@Transactional(readOnly = true)
fun <T> SampleImageRepository.findByIdOrNull(id: String, type: Class<T>): T? {
    return findById(id, type).orElse(null)
}
