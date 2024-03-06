package com.leijendary.extension

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder
import co.elastic.clients.json.JsonData
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.core.geo.GeoPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

fun Builder.shouldMatch(query: String, vararg fields: String) = apply {
    fields.forEach { field ->
        should { should ->
            should.match { it.query(query).field(field) }
        }
    }
}

fun Builder.mustMatch(query: String, vararg fields: String) = apply {
    fields.forEach { field ->
        must { must ->
            must.match { it.query(query).field(field) }
        }
    }
}

fun Builder.shouldWildcard(query: String, vararg fields: String) = apply {
    fields.forEach { field ->
        should { should ->
            should.wildcard { it.value("*$query*").field(field).caseInsensitive(true) }
        }
    }
}

fun Builder.mustWildcard(query: String, vararg fields: String) = apply {
    fields.forEach { field ->
        must { must ->
            must.wildcard { it.value("*$query*").field(field).caseInsensitive(true) }
        }
    }
}

fun Builder.geoDistance(geoPoint: GeoPoint, radius: Double, field: String) = apply {
    must { must ->
        must.geoDistance { geoDistance ->
            geoDistance.distance("${radius}km").field(field).location { location ->
                location.latlon { it.lat(geoPoint.lat).lon(geoPoint.lon) }
            }
        }
    }
}

fun Builder.dateRange(start: LocalDate, end: LocalDate?, field: String) = apply {
    must { must ->
        must.range { range ->
            range.field(field)
                .gte(JsonData.of(start.toString()))
                .format(DateFormat.date.name)
                .apply {
                    end?.let { lte(JsonData.of(it.toString())) }
                }
        }
    }
}

fun Builder.dateTimeRange(start: LocalDateTime, end: LocalDateTime?, field: String) = apply {
    must { must ->
        must.range { range ->
            range.field(field)
                .gte(JsonData.of(start.format(ISO_LOCAL_DATE_TIME)))
                .apply {
                    end?.let { lte(JsonData.of(it.format(ISO_LOCAL_DATE_TIME))) }
                }
        }
    }
}

fun Builder.dateTimeRange(start: OffsetDateTime, end: OffsetDateTime?, field: String) = apply {
    must { must ->
        must.range { range ->
            range.field(field)
                .gte(JsonData.of(start.toString()))
                .apply {
                    end?.let { lte(JsonData.of(it.toString())) }
                }
        }
    }
}

fun Builder.withinAnyDate(dates: Set<LocalDate>, field: String) = apply {
    dates.map { JsonData.of(it.toString()) }.forEach { date ->
        should { should ->
            should.range { it.field(field).gte(date).lte(date).format(DateFormat.date.name) }
        }
    }

    minimumShouldMatch("1")
}
