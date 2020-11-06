package com.yxt.crud.interceptor;

/**
 * @Author: Yanxt7
 * @Desc: null值转换策略
 * @Date: 2020/3/15
 */
public interface IResultNullFieldStrategy {

    Object transform(String type, Object object);
}
