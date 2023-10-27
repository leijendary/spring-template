package com.leijendary.spring.core.extension

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder
import co.elastic.clients.json.JsonData
import com.leijendary.spring.core.util.RequestContext.timeZone
import org.springframework.data.elasticsearch.annotations.DateFormat.date
import org.springframework.data.elasticsearch.core.geo.GeoPoint
import java.time.LocalDate
import java.time.OffsetDateTime

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

fun Builder.dateTimeRange(start: LocalDate, end: LocalDate?, field: String) = apply {
    val format = date.name
    val timeZoneId = timeZone.id

    must { must ->
        must.range { range ->
            range.field(field)
                .gte(JsonData.of(start.toString()))
                .format(format)
                .timeZone(timeZoneId)
                .apply {
                    end?.let { lte(JsonData.of(it.toString())) }
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
    val format = date.name
    val timeZoneId = timeZone.id

    dates.stream()
        .map(LocalDate::toString)
        .map(JsonData::of)
        .forEach { date ->
            should { should ->
                should.range {
                    it.field(field).gte(date).lte(date).format(format).timeZone(timeZoneId)
                }
            }
        }

    minimumShouldMatch("1")
}
