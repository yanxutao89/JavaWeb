package com.yxt.crud.interceptor;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/3/17
 */
public enum UserStatusEnum {

    ;

    String key;
    String value;

    UserStatusEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static String getValue(String key) {
        UserStatusEnum[] values = values();
        for (UserStatusEnum em : values) {
            if (key.equals(em.getKey())) {
                return em.getValue();
            }
        }
        return null;
    }
}
