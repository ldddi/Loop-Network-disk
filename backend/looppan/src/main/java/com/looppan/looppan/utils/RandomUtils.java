package com.looppan.looppan.utils;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtils {

    private static final Random RANDOM = new Random();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // 生成指定范围内的随机整数
    public static int generateRandomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max");
        }
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    // 生成指定范围内的随机长整数
    public static long generateRandomLong(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max");
        }
        return min + (long) (SECURE_RANDOM.nextDouble() * (max - min));
    }

    // 生成指定范围内的随机浮点数
    public static double generateRandomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max");
        }
        return min + (SECURE_RANDOM.nextDouble() * (max - min));
    }

    // 生成指定长度的随机字符串
    public static String generateRandomString(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length cannot be less than 1");
        }
        StringBuilder sb = new StringBuilder(length);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
