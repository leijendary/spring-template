package com.leijendary.spring.core.extension

import com.leijendary.spring.core.util.RequestContext.locale
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle.MEDIUM
import java.time.format.FormatStyle.SHORT

fun OffsetDateTime.localeFormatted(): String {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(MEDIUM, SHORT).withLocale(locale)

    return format(formatter)
}
