package com.payment.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class CryptoUtilsTest {
    @Test
    public void cryptoTest() {
        String text = "YeoSeonAssignment~";

        String encryptText = CryptoUtils.encrypt(text);
        log.info("Encrypt Text : " + encryptText);
        String decryptText = CryptoUtils.decrypt(encryptText);
        log.info("Decrypt Text : " + decryptText);

        assertEquals(text, decryptText);
    }

}
