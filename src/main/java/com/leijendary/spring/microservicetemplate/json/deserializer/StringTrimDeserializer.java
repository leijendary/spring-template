package com.leijendary.spring.microservicetemplate.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

import static java.util.Optional.ofNullable;

@JsonComponent
public class StringTrimDeserializer extends StringDeserializer {

    @Override
    public String deserialize(final JsonParser jsonParser,
                              final DeserializationContext deserializationContext) throws IOException {
        // Get the result from the original StringDeserializer
        final var deserialized = super.deserialize(jsonParser, deserializationContext);

        // ... and just trim it
        return ofNullable(deserialized)
                .map(String::trim)
                .orElse(deserialized);
    }
}
