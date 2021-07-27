package com.leijendary.spring.microservicetemplate.util;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class NumberUtil {

    public static boolean isInt(final String s) {
        try {
            parseInt(s);

            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static boolean isLong(final String s) {
        try {
            parseLong(s);

            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}
