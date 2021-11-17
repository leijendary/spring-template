package com.leijendary.spring.boot.template.util;

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
            builder.append(upperCaseFirst(string));
        }

        final var result = builder.toString();

        if (!capitalizeFirst) {
            return lowerCaseFirst(result);
        }

        return result;
    }

    public static String upperCaseFirst(final String value) {
        final char[] chars = value.toCharArray();
        chars[0] = toUpperCase(chars[0]);

        return new String(chars);
    }

    public static String lowerCaseFirst(final String value) {
        final char[] chars = value.toCharArray();
        chars[0] = toLowerCase(chars[0]);

        return new String(chars);
    }
}
