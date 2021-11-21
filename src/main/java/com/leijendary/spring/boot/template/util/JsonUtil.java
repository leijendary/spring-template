package com.leijendary.spring.boot.template.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

    private static final ObjectMapper MAPPER = SpringContext.getBean(ObjectMapper.class);

    public static <T> T toClass(final String json, final Class<T> type) throws JsonProcessingException {
        return MAPPER.readValue(json, type);
    }

    public static <T> T toClass(final Object object, final Class<T> type) {
        return MAPPER.convertValue(object, type);
    }

    public static String toJson(final Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            log.warn("Failed to parse object to json", e);
        }

        return null;
    }
}
