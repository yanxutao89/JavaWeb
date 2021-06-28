package com.user.utils;

import java.security.MessageDigest;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2021/6/19 17:37
 */
public final class Util {
    private static final AtomicLong SEQUENCE = new AtomicLong(0L);
    private static final int SEQUENCE_COUNT = 4;

    public static Long nextId() {
        long time = System.nanoTime();
        String sequence = String.valueOf(SEQUENCE.incrementAndGet());
        int length = sequence.length();
        StringBuilder sb = new StringBuilder();
        sb.append(time);
        if (length < SEQUENCE_COUNT) {
            for (int i = 0; i < SEQUENCE_COUNT - length; ++i) {
                sb.append(0);
            }
            sb.append(sequence);
        }
        else {
           sb.append(sequence, 0, SEQUENCE_COUNT);
        }
        return Long.parseLong(sb.toString());
    }

    public static String getMd5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
