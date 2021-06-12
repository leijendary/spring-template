package com.leijendary.spring.microservicetemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.misc.Unsafe;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // Disable devtools restart for avro
        System.setProperty("spring.devtools.restart.enabled", "false");

        disableIllegalAccessLoggerWarning();

        SpringApplication.run(Application.class, args);
    }

    public static void disableIllegalAccessLoggerWarning() {
        try {
            final var theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);

            final var unsafe = (Unsafe) theUnsafe.get(null);
            final var cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            final var logger = cls.getDeclaredField("logger");

            unsafe.putObjectVolatile(cls, unsafe.staticFieldOffset(logger), null);
        } catch (final Exception ignored) { }
    }
}
