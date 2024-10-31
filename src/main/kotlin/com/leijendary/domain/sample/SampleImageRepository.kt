package com.leijendary.domain.sample

import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(readOnly = true)
interface SampleImageRepository : CrudRepository<SampleImage, String> {
    fun <T> findById(id: String, type: Class<T>): Optional<T>
}
