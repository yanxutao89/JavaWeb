package com.user.utils;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;


/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/8/27 22:13
 */
@Component
@Intercepts(value = {
        @Signature(type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
                        CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class MySqlLogger implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object target = invocation.getTarget();
        Object result = invocation.proceed();
        try {
            if (target instanceof Executor) {
                Object[] args = invocation.getArgs();
                MappedStatement ms = (MappedStatement) args[0];
                Object parameterObject = args[1];
                BoundSql boundSql = ms.getBoundSql(parameterObject);
                String sql = formatSql(boundSql.getSql());
                if (parameterObject instanceof Map) {
                    Map parameterMap = (Map) parameterObject;
                    for (Object entry : parameterMap.entrySet()) {
                        Map.Entry e = (Map.Entry) entry;
                        Object value = e.getValue();
                        if (value instanceof Collection) {
                            Collection values = (Collection) value;
                            for (Object v : values) {
                                sql = sql.replaceFirst("\\?", convert2SqlData(v).toString());
                            }
                        } else {
                            sql = sql.replaceFirst("\\?", convert2SqlData(value).toString());
                        }
                    }
                }
                System.err.println(sql);
            }
        } catch (Exception e) {

        }

        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private String formatSql(String sql) {
        if (null == sql) {
            return "";
        }
        sql = sql.replaceAll("(?m)^\\s*$" + System.lineSeparator(), "")
            .replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        return sql;
    }

    private Object convert2SqlData(Object o) {
        if (o instanceof String) {
            return "'" + o + "'";
        }
        return o;
    }

}
