package com.leijendary.spring.template.core.model

import com.leijendary.spring.template.core.entity.UUIDEntity
import com.leijendary.spring.template.core.extension.reflectGet
import org.springframework.data.domain.Sort

const val FIELD_ROW_ID = "rowId"

class SeekToken {
    val fields: Map<String, Any?>

    /**
     * Needed for json deserialization
     */
    constructor() {
        this.fields = emptyMap()
    }

    constructor(last: UUIDEntity, sort: Sort) {
        this.fields = getFields(last, sort)
    }

    private fun getFields(last: UUIDEntity, sort: Sort): Map<String, Any?> {
        val fieldValueMap = mutableMapOf<String, Any?>()

        sort
            .filter { it.property != FIELD_ROW_ID }
            .forEach {
                val property = it.property

                fieldValueMap[property] = last.reflectGet(property)
            }

        fieldValueMap[FIELD_ROW_ID] = last.rowId

        return fieldValueMap
    }
}
