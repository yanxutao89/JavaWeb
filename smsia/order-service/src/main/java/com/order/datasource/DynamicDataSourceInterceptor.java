package com.order.datasource;


import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2021/1/3 13:29
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })
            })
public class DynamicDataSourceInterceptor implements Interceptor {
    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement) objects[0];
        String id = ms.getId();
        String value = CACHE.get(id);
        if (null == value) {
            TargetDataSource dataSource = getDataSourceAnnotation(id);
            if (null != dataSource) {
                value = dataSource.value();
                CACHE.put(id, value);
                DynamicDataSourceContextHolder.setDataSourceType(value);
            }
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private TargetDataSource getDataSourceAnnotation(String id) {
        TargetDataSource dataSource = null;
        try {
            int i = id.lastIndexOf(".");
            if (i != -1) {
                String className = id.substring(0, i);
                String methodName = id.substring(i + 1);
                final Method[] method = Class.forName(className).getMethods();
                for (Method me : method) {
                    if (me.getName().equals(methodName) && me.isAnnotationPresent(TargetDataSource.class)) {
                        dataSource = me.getAnnotation(TargetDataSource.class);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dataSource;
    }
}
