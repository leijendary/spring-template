package com.leijendary.spring.boot.template.core.util;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.DOWN;
import static java.math.RoundingMode.FLOOR;
import static java.nio.ByteBuffer.wrap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public class NumberUtil {

    public static final int SCALE = 4;

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

    public static BigDecimal multiply(final BigDecimal multiplier, final BigDecimal multiplicand) {
        return multiplier
                .multiply(multiplicand)
                .setScale(SCALE, DOWN);
    }

    public static BigDecimal percentage(final BigDecimal number, final BigDecimal percent) {
        if (number.compareTo(ZERO) == 0 || percent.compareTo(ZERO) == 0) {
            return ZERO;
        }

        final var divisor = divisor(percent);

        return divide(number, divisor);
    }

    public static BigDecimal divisor(final BigDecimal percent) {
        return divide(valueOf(100), percent);
    }

    public static BigDecimal divide(final BigDecimal number, final BigDecimal divisor) {
        return number.divide(divisor, SCALE, DOWN);
    }

    public static ByteBuffer toByteBuffer(final BigDecimal bigDecimal) {
        final var bytes = bigDecimal.setScale(SCALE, FLOOR)
                .unscaledValue()
                .toByteArray();

        return wrap(bytes);
    }

    public static BigDecimal toBigDecimal(final ByteBuffer byteBuffer) {
        final var bytes = byteBuffer.array();
        final var bigInteger = new BigInteger(bytes);

        return new BigDecimal(bigInteger, SCALE);
    }
}
