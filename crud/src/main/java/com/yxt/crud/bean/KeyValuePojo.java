package com.yxt.crud.bean;

import java.util.Objects;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/06 21:15
 */
public class KeyValuePojo {

    private String keyValueId;          // 键值ID
    private String enumName;            // 枚举名称
    private String type;                // 类型
    private String key;                 // 键
    private String value;               // 值

    public String getKeyValueId() {
        return keyValueId;
    }

    public void setKeyValueId(String keyValueId) {
        this.keyValueId = keyValueId;
    }

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyValuePojo)) return false;
        KeyValuePojo that = (KeyValuePojo) o;
        return Objects.equals(getKeyValueId(), that.getKeyValueId()) &&
                Objects.equals(getEnumName(), that.getEnumName()) &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getKey(), that.getKey()) &&
                Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKeyValueId(), getEnumName(), getType(), getKey(), getValue());
    }

    @Override
    public String toString() {
        return "KeyValuePojo{" +
                "keyValueId='" + keyValueId + '\'' +
                ", enumName='" + enumName + '\'' +
                ", type='" + type + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
