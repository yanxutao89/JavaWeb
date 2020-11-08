package com.yxt.crud.interceptor;

import com.yxt.crud.annotations.Converter;
import com.yxt.crud.utils.EnumUtils;
import com.yxt.crud.utils.RedisUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: Yanxt7
 * @Desc: null值转换拦截器
 * @Date: 2020/3/13
 */
@Component
@Intercepts({
        @Signature(
                type = ResultSetHandler.class,
                method = "handleResultSets",
                args = {Statement.class}
        )
})
public class MyResultSetHandlerInterceptor implements Interceptor {

    private static Logger LOGGER = LogManager.getLogger(MyResultSetHandlerInterceptor.class);

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();
    private static final String MAPPED_STATEMENT = "mappedStatement";
    private static final String BOUND_SQL = "boundSql";
    private static final String REDIS_KEY = "CrudMapping";

    @Autowired
    private RedisUtils redisUtils;

    private Properties properties = new Properties();

    public Object intercept(Invocation invocation) throws Throwable {

        Object result = invocation.proceed();
        ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
        MetaObject metaResultSetHandler = MetaObject.forObject(resultSetHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, REFLECTOR_FACTORY);
        if (metaResultSetHandler.hasGetter(MAPPED_STATEMENT)) {

            MappedStatement mappedStatement = (MappedStatement) metaResultSetHandler.getValue(MAPPED_STATEMENT);
            String id = mappedStatement.getId();
            Converter annotation = getConverterAnnotation(id);
            if (annotation != null && result != null) {

                String[] properties = annotation.properties();
                Class[] enums = annotation.enums();
                Map<String, String>[] converterMap = new HashMap[enums.length];
                for (int i = 0; i < enums.length; ++i) {
                    converterMap[i] = EnumUtils.getEnumMap(enums[i]);
                }
                Class<? extends IResultNullFieldStrategy> transformStrategyClazz = annotation.transformStrategy();
                IResultNullFieldStrategy strategy = transformStrategyClazz.newInstance();
                try {
                    long start = System.currentTimeMillis();
                    Map<String, String>  unMappedColumn = getUnMappedColumn(metaResultSetHandler, invocation, mappedStatement.getId());
                    handleRecursively(result, mappedStatement, unMappedColumn, strategy, properties, converterMap, 0);
                    LOGGER.info(String.format("Time consumed for %s is %d ms", id, (System.currentTimeMillis() - start)));
                } catch (Exception e) {
                    LOGGER.error(String.format("Something of %s occurred while handling the result sets for %s, which can be neglected", e.getMessage(), id));
                }
            }
        }

        return result;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    private Converter getConverterAnnotation(String id) {

        Converter converter = null;
        try {
            int i = id.lastIndexOf(".");
            if (i != -1) {
                String className = id.substring(0, i);
                String methodName = id.substring(i + 1);
                final Method[] method = Class.forName(className).getMethods();
                for (Method me : method) {
                    if (me.getName().equals(methodName) && me.isAnnotationPresent(Converter.class)) {
                        converter = me.getAnnotation(Converter.class);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return converter;
    }

    private void handleRecursively(Object result, MappedStatement mappedStatement, Map<String, String>  unMappedColumn, IResultNullFieldStrategy strategy, String[] properties, Map<String, String>[] converterMap, int depth) throws Exception {
        if (notInExcludedResultTypes(result) && depth < 2) {
            if (result instanceof List<?>) {
                List<?> list = (List<?>) result;
                int size = list.size();
                for (int index = 0; index < size; ++index) {
                    Object object = list.get(index);
                    if (notInExcludedResultTypes(object)) {
                        handleRecursively(object, mappedStatement, unMappedColumn, strategy, properties, converterMap, depth + 1);
                    } else {
                        return;
                    }
                }
            } else {
                if (result instanceof Map) {
                    Map map= (Map) result;
                    for (Object key : map.keySet()) {
                        Object value = map.get(key);
                        if (notInExcludedResultTypes(value)) {
                            handleRecursively(value, mappedStatement, unMappedColumn, strategy, properties, converterMap, depth + 1);
                        } else {
                            if (value == null) {
                                map.put(key, strategy.transform(unMappedColumn.get(key), value));
                            } else {
                                for (int i = 0; i < properties.length; ++i) {
                                    if (properties[i].equals(key)) {
                                        for (Object e : converterMap[i].keySet()) {
                                            if (value.equals(e)) {
                                                map.put(key, converterMap[i].get(e));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    MetaObject beanItem = MetaObject.forObject(result, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, REFLECTOR_FACTORY);
                    List<ResultMap> resultMaps = mappedStatement.getResultMaps();
                    if (resultMaps.size() > 0) {
                        ResultMap resultMap = resultMaps.get(0);
                        Class<?> clazz = resultMap.getType();
                        for (Field field : clazz.getDeclaredFields()) {
                            String fieldName = field.getName();
                            if (beanItem.hasGetter(fieldName)) {
                                Object fieldValue = beanItem.getValue(fieldName);
                                if (fieldValue == null) {
                                    beanItem.setValue(fieldName, strategy.transform(field.getType().getName(), fieldValue));
                                } else {
                                    for (int i = 0; i < properties.length; ++i) {
                                        if (properties[i].equals(fieldName)) {
                                            for (Object key : converterMap[i].keySet()) {
                                                if (fieldValue.equals(key)) {
                                                    beanItem.setValue(fieldName, converterMap[i].get(key));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            return;
        }
    }

    private Map<String, String> getUnMappedColumn(MetaObject metaResultSetHandler, Invocation invocation, String id) throws Exception {

        // Get mapping from redis
        Map map = redisUtils.hmget(REDIS_KEY);
        map = map == null ? new HashMap<String, Map<String, String>>() : map;
        Map mapping = (Map<String, String>)map.get(id);
        if (mapping == null) {

            synchronized (this) {
                // Get mapping from redis
                map = redisUtils.hmget(REDIS_KEY);
                map = map == null ? new HashMap<String, Map<String, String>>() : map;
                mapping = (Map<String, String>)map.get(id);
                if(mapping != null) {
                    return mapping;
                }

                // Generate mapping from mysql
                mapping = new HashMap();
                if (metaResultSetHandler.hasGetter(BOUND_SQL)) {

                    BoundSql boundSql = (BoundSql) metaResultSetHandler.getValue(BOUND_SQL);
                    Statement statement = (Statement) invocation.getArgs()[0];
                    String sql = formatSql(boundSql.getSql());
                    ResultSet resultSet = statement.executeQuery(sql);
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    for (int i = 0; i < metaData.getColumnCount(); ++i) {
                        String columnLabel = metaData.getColumnLabel(i + 1);
                        String columnClassName = metaData.getColumnClassName(i + 1);
                        mapping.put(columnLabel, columnClassName);
                    }

                    map.put(id, mapping);
                    // Put mapping into redis
                    redisUtils.hmset(REDIS_KEY, map);
                }
            }
        }

        return mapping;
    }

    private String formatSql(String sql) {

        int where = sql.toLowerCase().lastIndexOf("where");
        if (-1 != where) {
            sql = sql.substring(0, where);
        }
        sql = sql.replaceAll("(\\?){1}", "''");

        return sql + " limit 1";
    }

    /**
     * 排除对基本类型,基本类型包装类型以及String类型的null值转换
     * 按照上述类型在实际应用中出现频率从高到低依次判断,以便更快返回结果
     * @param object
     * @return
     */
    private boolean notInExcludedResultTypes(Object object){

        if (object == null) {
            return false;
        }
        if (object.getClass() == int.class || object instanceof Integer) {
            return false;
        }
        if (object instanceof String) {
            return false;
        }
        if (object.getClass() == boolean.class || object instanceof Boolean) {
            return false;
        }
        if (object.getClass() == double.class || object instanceof Double) {
            return false;
        }
        if (object.getClass() == float.class || object instanceof Float) {
            return false;
        }
        if (object.getClass() == long.class || object instanceof Long) {
            return false;
        }
        if (object.getClass() == short.class || object instanceof Short) {
            return false;
        }
        if (object.getClass() == byte.class || object instanceof Byte) {
            return false;
        }
        if (object.getClass() == char.class || object instanceof Character) {
            return false;
        }

        return true;
    }
}
