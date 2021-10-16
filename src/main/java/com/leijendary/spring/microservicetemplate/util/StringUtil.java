package com.leijendary.spring.microservicetemplate.util;

import static java.lang.Character.toLowerCase;
import static java.lang.Character.toUpperCase;

public class StringUtil {

    public static String snakeCaseToCamelCase(final String value) {
        return snakeCaseToCamelCase(value, false);
    }

    public static String snakeCaseToCamelCase(final String value, final boolean capitalizeFirst) {
        final var builder = new StringBuilder();
        final var strings = value.split("_");

        for (final String string: strings) {
            final char[] chars = string.toCharArray();
            chars[0] = toUpperCase(chars[0]);

            builder.append(new String(chars));
        }

        var result = builder.toString();

        if (!capitalizeFirst) {
            final char[] chars = result.toCharArray();
            chars[0] = toLowerCase(chars[0]);

            result = new String(chars);
        }

        return result;
    }
}
