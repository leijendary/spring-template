package com.leijendary.spring.template.core.extension

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery
import co.elastic.clients.util.ObjectBuilder

fun BoolQuery.Builder.shouldMatch(query: String, vararg names: String): ObjectBuilder<BoolQuery> {
    return should { should ->
        should.match { match ->
            names.forEach { name ->
                match.field(name).query(query).fuzziness("AUTO")
            }

            match
        }
    }
}

fun BoolQuery.Builder.shouldWildcard(query: String, vararg names: String): ObjectBuilder<BoolQuery> {
    return should { should ->
        should.wildcard { wildcard ->
            names.forEach { name ->
                wildcard.field(name).value("*$query*").caseInsensitive(true)
            }

            wildcard
        }
    }
}