package com.willy.Church.util;


import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordUtil {

    private static final SecureRandom RAND = new SecureRandom();

    public static String hash(String rawPassword) {
        byte[] salt = new byte[16];
        RAND.nextBytes(salt);
        String saltedHash = base64(sha256(rawPassword + Base64.getEncoder().encodeToString(salt)));
        return Base64.getEncoder().encodeToString(salt) + ":" + saltedHash;
    }

    public static boolean matches(String rawPassword, String stored) {
        String[] parts = stored.split(":");
        if (parts.length != 2) return false;
        String salt = parts[0];
        String expectedHash = parts[1];
        String computedHash = base64(sha256(rawPassword + salt));
        return MessageDigest.isEqual(expectedHash.getBytes(), computedHash.getBytes());
    }

    private static byte[] sha256(String input) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(input.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String base64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private PasswordUtil() {}
}