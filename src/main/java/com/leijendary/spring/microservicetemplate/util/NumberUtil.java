package com.leijendary.spring.microservicetemplate.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.FLOOR;
import static java.math.RoundingMode.HALF_UP;
import static java.nio.ByteBuffer.wrap;

public class NumberUtil {

    public static final int SCALE = 2;

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

    public static BigDecimal multiply(final BigDecimal multiplier, final int multiplicand) {
        return multiplier
                .multiply(valueOf(multiplicand))
                .setScale(SCALE, HALF_UP);
    }

    public static BigDecimal percentage(final BigDecimal number, final BigDecimal divisor) {
        final var percent = valueOf(100).divide(divisor, HALF_UP);

        return number.divide(percent, HALF_UP);
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
