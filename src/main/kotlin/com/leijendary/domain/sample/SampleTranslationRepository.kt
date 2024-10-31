package com.leijendary.domain.sample

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.ListCrudRepository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface SampleTranslationRepository : ListCrudRepository<SampleTranslation, String> {
    @Query("select * from sample_translation where id = :id order by (language = :language)::int desc, ordinal limit 1")
    fun findFirstByIdAndLanguage(id: String, language: String): SampleTranslation?

    fun <T> findById(id: String, type: Class<T>): List<T>
}
