package com.yxt.crud.interceptor;

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
import java.util.regex.Pattern;


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
public class MySqlLogInterceptor implements Interceptor {

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
                String sql = boundSql.getSql();
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
        Pattern blankLine = Pattern.compile("(^(\\s*)\\r\\n)*" +
                "|(\\n\\s*\\r)*");
        sql = blankLine.matcher(sql).replaceAll("");
        return sql;
    }

    private Object convert2SqlData(Object o) {
        if (o instanceof String) {
            return "'" + o + "'";
        }
        return o;
    }

}
