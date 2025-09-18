package com.leijendary.domain.sample

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.ListCrudRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

private const val QUERY_FIND_BY_ID_AND_LANGUAGE = """
SELECT *
FROM sample_translation
WHERE id = :id
ORDER BY (language = :language)::int DESC, ordinal
LIMIT 1
"""

@Transactional(readOnly = true)
interface SampleTranslationRepository : ListCrudRepository<SampleTranslation, String> {
    @Query(QUERY_FIND_BY_ID_AND_LANGUAGE)
    fun findByIdAndLanguage(id: String, language: String): Optional<SampleTranslation>
}
