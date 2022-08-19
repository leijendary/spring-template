package com.leijendary.spring.template.projection

import com.leijendary.spring.template.core.projection.CreatedProjection
import com.leijendary.spring.template.core.projection.LastModifiedProjection
import com.leijendary.spring.template.core.projection.LocaleProjection
import com.leijendary.spring.template.core.projection.LocalizedProjection
import com.leijendary.spring.template.projection.SampleTableProjection.SampleTableTranslationsProjection
import java.math.BigDecimal

interface SampleTableProjection : LocalizedProjection<SampleTableTranslationsProjection>, CreatedProjection,
    LastModifiedProjection {
    var column1: String
    var column2: Int
    var amount: BigDecimal

    interface SampleTableTranslationsProjection : LocaleProjection {
        var name: String
        var description: String
    }
}