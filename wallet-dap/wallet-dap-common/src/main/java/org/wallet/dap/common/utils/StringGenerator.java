package org.wallet.dap.common.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 随机字符串生成器
 * @author zengfucheng
 **/
public class StringGenerator {

    private static final char[] DEFAULT_CODEC = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            .toCharArray();

    private static StringGenerator generator = new StringGenerator();

    private Random random = new SecureRandom();

    private StringGenerator(){}

    public static StringGenerator newInstance(){
        return generator;
    }

    public String generate(int length) {
        byte[] verifierBytes = new byte[length];
        random.nextBytes(verifierBytes);
        return getAuthorizationCodeString(verifierBytes);
    }

    public String generate() {
        int length = 32;
        byte[] verifierBytes = new byte[length];
        random.nextBytes(verifierBytes);
        return getAuthorizationCodeString(verifierBytes);
    }

    /**
     * Convert these random bytes to a verifier string. The default implementation mods the bytes to fit into the
     * ASCII letters 1-9, A-Z, a-z .
     *
     * @param verifierBytes The bytes.
     * @return The string.
     */
    private String getAuthorizationCodeString(byte[] verifierBytes) {
        char[] chars = new char[verifierBytes.length];
        for (int i = 0; i < verifierBytes.length; i++) {
            chars[i] = DEFAULT_CODEC[((verifierBytes[i] & 0xFF) % DEFAULT_CODEC.length)];
        }
        return new String(chars);
    }

}
