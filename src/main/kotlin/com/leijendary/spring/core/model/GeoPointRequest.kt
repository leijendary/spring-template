package com.leijendary.spring.core.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.elasticsearch.core.geo.GeoPoint

open class GeoPointRequest(open val latitude: Double?, open val longitude: Double?) {
    val radius: Double = 50.0

    @get:JsonIgnore
    val geoPoint: GeoPoint?
        get() {
            if (latitude == null || longitude == null) {
                return null
            }

            return GeoPoint(latitude!!, longitude!!)
        }
}
