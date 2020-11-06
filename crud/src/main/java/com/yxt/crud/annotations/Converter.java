package com.yxt.crud.annotations;

import com.yxt.crud.interceptor.IResultNullFieldStrategy;
import com.yxt.crud.interceptor.Null2NonNullStrategy;

import java.lang.annotation.*;

/**
 * @Author: Yanxt7
 * @Desc: 转换注解
 * @Date: 2020/3/14
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Converter {

    /**
     * null值转换策略
     * @return
     */
    Class<? extends IResultNullFieldStrategy> transformStrategy() default Null2NonNullStrategy.class;

    /**
     * 转换属性值
     * @return
     */
    String[] properties() default {};

    /**
     * 转换枚举类
     * @return
     */
    Class[] enums() default {};
}