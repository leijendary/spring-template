package com.leijendary.spring.boot.template.core.data

import com.leijendary.spring.boot.template.core.extension.reflectGet
import com.leijendary.spring.boot.template.core.model.UUIDModel
import org.springframework.data.domain.Sort

const val ROW_ID_FIELD = "rowId"

class SeekToken {
    val fields: Map<String, Any?>

    constructor() {
        this.fields = emptyMap()
    }

    constructor(last: UUIDModel, sort: Sort) {
        this.fields = getFields(last, sort)
    }

    private fun getFields(last: UUIDModel, sort: Sort): Map<String, Any?> {
        val fieldValueMap = mutableMapOf<String, Any?>()

        sort
            .filter { it.property != ROW_ID_FIELD }
            .forEach {
                val property = it.property

                fieldValueMap[property] = last.reflectGet(property)
            }

        fieldValueMap[ROW_ID_FIELD] = last.rowId

        return fieldValueMap
    }
}