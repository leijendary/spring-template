package com.leijendary.spring.boot.template.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static final ObjectMapper MAPPER = SpringContext.getBean(ObjectMapper.class);

    public static <T> T toClass(final String json, final Class<T> type) throws JsonProcessingException {
        return MAPPER.readValue(json, type);
    }

    public static <T> T toClass(final Object object, final Class<T> type) {
        return MAPPER.convertValue(object, type);
    }

    public static String toJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }
}
