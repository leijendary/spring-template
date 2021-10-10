package com.leijendary.spring.microservicetemplate.mapper;

import org.mapstruct.Mapper;

import java.time.OffsetDateTime;

import static com.leijendary.spring.microservicetemplate.util.RequestContext.getTimeZone;
import static java.time.Instant.ofEpochMilli;
import static java.time.OffsetDateTime.ofInstant;
import static org.mapstruct.factory.Mappers.getMapper;

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
