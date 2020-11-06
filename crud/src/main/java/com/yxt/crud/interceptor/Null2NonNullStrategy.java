package com.yxt.crud.interceptor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Author: Yanxt7
 * @Desc: null值转换字符串
 * @Date: 2020/3/15
 */
public class Null2NonNullStrategy implements IResultNullFieldStrategy {

    @Override
    public Object transform(String type, Object object) {

        if ("java.lang.String".equals(type)) {
            return "";
        }
        if ("java.sql.Timestamp".equals(type)) {
            return new Date(0);
        }
        if ("java.util.Date".equals(type)) {
            return new Date(0);
        }
        if ("java.sql.Date".equals(type)) {
            return new Date(0);
        }
        if ("java.lang.byte[]".equals(type)) {
            return new byte[0];
        }
        if ("java.lang.Long".equals(type)) {
            return 0L;
        }
        if ("java.lang.Integer".equals(type)) {
            return 0;
        }
        if ("java.lang.Boolean".equals(type)) {
            return false;
        }
        if ("java.math.BigInteger".equals(type)) {
            return new BigInteger("0");
        }
        if ("java.lang.Float".equals(type)) {
            return 0;
        }
        if ("java.lang.Double".equals(type)) {
            return 0;
        }
        if ("java.math.BigDecimal".equals(type)) {
            return new BigDecimal("0");
        }

        return object;
    }
}
