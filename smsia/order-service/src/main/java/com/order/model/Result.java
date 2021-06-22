package com.order.model;

import java.io.Serializable;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2021/6/20 10:16
 */
public class Result implements Serializable {
    private Integer code = -1;
    private String msg;
    private Object data;

    public Integer getCode() {
        return code;
    }

    public Result setCode(Integer code) {
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

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
