package com.leijendary.spring.boot.template.core.json;

import java.io.IOException;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class OffsetDateTimeToEpochMillisSerializer extends StdSerializer<OffsetDateTime> {

    public OffsetDateTimeToEpochMillisSerializer() {
        this(null);
    }

    protected OffsetDateTimeToEpochMillisSerializer(final Class<OffsetDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(final OffsetDateTime offsetDateTime, final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        final var epochMillis = offsetDateTime.toInstant().toEpochMilli();

        jsonGenerator.writeNumber(epochMillis);
    }
}
