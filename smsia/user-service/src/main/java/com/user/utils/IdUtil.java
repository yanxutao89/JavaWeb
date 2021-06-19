package com.user.utils;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2021/6/19 17:37
 */
public final class IdUtil {

    public static Long nextId() {
        return System.nanoTime();
    }

}
