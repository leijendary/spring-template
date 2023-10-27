package com.leijendary.spring.core.model

import com.leijendary.spring.core.extension.reflectGet
import com.leijendary.spring.core.projection.IdentityProjection
import org.springframework.data.domain.Sort

const val FIELD_ID = "id"

class SeekToken {
    val fields: Map<String, Any?>

    /**
     * Needed for json deserialization
     */
    constructor() {
        this.fields = emptyMap()
    }

    constructor(last: IdentityProjection, sort: Sort) {
        this.fields = getFields(last, sort)
    }

    private fun getFields(last: IdentityProjection, sort: Sort): Map<String, Any?> {
        val fieldValueMap = mutableMapOf<String, Any?>()

        sort.filter { it.property != FIELD_ID }.forEach {
            val property = it.property

            fieldValueMap[property] = last.reflectGet(property)
        }

        fieldValueMap[FIELD_ID] = last.id

        return fieldValueMap
    }
}
