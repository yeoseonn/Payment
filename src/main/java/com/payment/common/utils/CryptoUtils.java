package com.payment.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

@Slf4j
public class CryptoUtils {
    private static SecretKey key;
    private static final String KEY_STRING = "YeoseonAPIProjectKeyString";
    private static final String AES = "AES";

    static {
        try {
            byte[] hash = MessageDigest.getInstance("SHA1").digest(KEY_STRING.getBytes(StandardCharsets.US_ASCII));
            byte[] keyBytes = Arrays.copyOf(hash, 16);
            key = new SecretKeySpec(keyBytes, AES);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static String encrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new BASE64Encoder().encode(cipher.doFinal(text.getBytes()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return StringUtils.EMPTY;
    }

    public static String decrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] bytes = new BASE64Decoder().decodeBuffer(text);
            return new String(cipher.doFinal(bytes));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return StringUtils.EMPTY;
    }
}