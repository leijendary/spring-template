package com.leijendary.spring.template.core.extension

import co.elastic.clients.elasticsearch._types.NestedSortValue
import co.elastic.clients.elasticsearch._types.SortOptions
import co.elastic.clients.elasticsearch._types.SortOrder
import co.elastic.clients.util.ObjectBuilder
import com.leijendary.spring.template.core.util.RequestContext
import org.springframework.data.domain.Sort

fun SortOptions.Builder.sortBuilder(sort: Sort): ObjectBuilder<SortOptions> {
    return field { field ->
        sort.forEach { order ->
            val property = order.property
            val direction = order.direction
            val sortOrder = if (direction.isAscending) SortOrder.Asc else SortOrder.Desc

            if (property.startsWith("translations.")) {
                val nestedSort = languageFilter()

                field.nested { nestedSort }.order(sortOrder)
            } else {
                field.field(property).order(sortOrder)
            }
        }

        field
    }
}

private fun languageFilter(): NestedSortValue.Builder {
    val language = RequestContext.language

    return NestedSortValue.Builder().nested { nested ->
        nested.path("translations").filter { filter ->
            filter.term { term ->
                term.field("translations.language").value(language)
            }

            filter
        }

        nested
    }
}