package com.yxt.crud.interceptor;

import com.yxt.crud.utils.PatternUtils;
import com.yxt.crud.utils.RedisUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
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
                args = {MappedStatement.class, Object.class})})
public class NonQuerySqlInterceptor implements Interceptor {

    private static final List<String> PUBLIC_TABLE_NAMES = new ArrayList<>(16);

    static {
    }

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object target = invocation.getTarget();
        Object result = invocation.proceed();
        try {
            if (target instanceof Executor) {

                Object[] args = invocation.getArgs();
                MappedStatement ms = (MappedStatement) args[0];
                String commandName = ms.getSqlCommandType().name().toLowerCase();
                Object parameterObject = args[1];
                BoundSql boundSql = ms.getBoundSql(parameterObject);
                String sql = boundSql.getSql();
                List<String> tableNames = new ArrayList<>();
                if(commandName.startsWith("insert")){
                    tableNames = PatternUtils.getPossibleTableNames(sql, "insert");
                } else if (commandName.startsWith("update")) {
                    tableNames = PatternUtils.getPossibleTableNames(sql, "update");
                } else if (commandName.startsWith("delete")) {
                    tableNames = PatternUtils.getPossibleTableNames(sql, "delete");
                }

                if (!CollectionUtils.isEmpty(tableNames)) {
                    String tableName = tableNames.get(0);
                    if (PUBLIC_TABLE_NAMES.contains(tableName)) {
                        redisUtils.deleteByPrefix("count:");
                    } else {
                        redisUtils.deleteByPrefix("count:" + tableName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

}
