package com.etc.paymentms.service;

import com.etc.paymentms.security.RsaKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.util.Base64;

/**
 * Provides RSA decryption services.
 */
@Service
@RequiredArgsConstructor
public class RsaService {

    private final RsaKeyGenerator keyGenerator;

    /**
     * Decrypts an encrypted value.
     *
     * @param value encrypted text
     * @return decrypted text
     */
    public String decrypt(
            String value) {

        try {

            Cipher cipher =
                    Cipher.getInstance("RSA");

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    keyGenerator.getPrivateKey()
            );

            byte[] decrypted =
                    cipher.doFinal(
                            Base64.getDecoder()
                                    .decode(value)
                    );

            return new String(decrypted);

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Error decrypting data",
                    ex
            );
        }
    }
}