package com.etc.paymentms.security;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * Loads the RSA private key used to decrypt sensitive information.
 */
@Component
public class RsaKeyGenerator {

    private PrivateKey privateKey;

    /**
     * Loads the RSA private key from the application resources.
     *
     * @throws Exception if the key cannot be loaded
     */
    @PostConstruct
    public void init() throws Exception {

        ClassPathResource resource =
                new ClassPathResource(
                        "keys/private.key"
                );

        String key = new String(
                resource.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        key = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decodedKey =
                Base64.getDecoder()
                        .decode(key);

        PKCS8EncodedKeySpec keySpec =
                new PKCS8EncodedKeySpec(decodedKey);

        KeyFactory keyFactory =
                KeyFactory.getInstance("RSA");

        this.privateKey =
                keyFactory.generatePrivate(
                        keySpec
                );
    }

    /**
     * Returns the RSA private key.
     *
     * @return private key
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}