package com.leijendary.spring.boot.template.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import static org.springframework.security.crypto.encrypt.Encryptors.delux;

@Component
@RequiredArgsConstructor
public class Encryption {

    private final KeyProperties keyProperties;

    public String encrypt(final String raw) {
        return encryptor().encrypt(raw);
    }

    public String decrypt(final String encrypted) {
        return encryptor().decrypt(encrypted);
    }

    private TextEncryptor encryptor() {
        final var key = keyProperties.getKey();
        final var salt = keyProperties.getSalt();

        return delux(key, salt);
    }
}
