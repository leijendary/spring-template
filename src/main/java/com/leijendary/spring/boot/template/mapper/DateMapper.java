package com.leijendary.spring.boot.template.mapper;

import static com.leijendary.spring.boot.template.util.RequestContext.getTimeZone;
import static java.time.Instant.ofEpochMilli;
import static java.time.OffsetDateTime.ofInstant;
import static org.mapstruct.factory.Mappers.getMapper;

import java.time.OffsetDateTime;

import org.mapstruct.Mapper;

@Mapper
public interface DateMapper {

    DateMapper INSTANCE = getMapper(DateMapper.class);

    default OffsetDateTime toOffsetDateTime(final long epochMillis) {
        final var instant = ofEpochMilli(epochMillis);
        final var zoneId = getTimeZone().toZoneId();

        return ofInstant(instant, zoneId);
    }

    default long toEpochMillis(final OffsetDateTime offsetDateTime) {
        return offsetDateTime.toInstant().toEpochMilli();
    }
}
