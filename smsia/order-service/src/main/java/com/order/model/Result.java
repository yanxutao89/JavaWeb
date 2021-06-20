package com.order.model;

import java.io.Serializable;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2021/6/20 10:16
 */
public class Result implements Serializable {
    private int code = -1;
    private String msg;
    private Object data;

    public int getCode() {
        return code;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }
}
