package com.yxt.crud.interceptor;

import com.yxt.crud.annotations.Converter;
import com.yxt.crud.bean.KeyValuePojo;
import com.yxt.crud.mapper.IBaseMapper;
import com.yxt.crud.mapper.IKeyValueMapper;
import com.yxt.crud.mapper.IUserMapper;
import com.yxt.crud.utils.EnumUtils;
import com.yxt.crud.utils.RedisUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc: 动态加载Enum
 * @Date: 2020/3/17
 */
@Component
public class MyApplicationRunner implements ApplicationRunner {

    private static Logger LOGGER = LogManager.getLogger(MyApplicationRunner.class);

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
	private IKeyValueMapper keyValueMapper;
    @Autowired
    private IUserMapper userMapper;
    @Autowired
    private RedisUtils redisUtils;

//    @Value("${spring.table.change}")
    private Boolean change = true;
//    @Value("${spring.internationalization.language}")
    private String language = "chinese";

    private static final String REDIS_KEY = "CmpResourceMapping";
    private static final List<IBaseMapper> MAPPER_INSTANCES = new ArrayList<>();
    private static final Map<Integer, IBaseMapper> MAPPER_INSTANCE_MAP = new HashMap<>();
    private static final Map<String, Class> ENUM_TYPE_MAP = new HashMap<>();

    static {
        ENUM_TYPE_MAP.put("StatusEnum", BaseEnum.class);
        ENUM_TYPE_MAP.put("ImageTypeEnum", UserStatusEnum.class);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        try {
            if (change) {
                // Clear data in redis
                redisUtils.del(REDIS_KEY);
                // Initialize dao instances
                initDaoInstances();
                String[] beanNames = applicationContext.getBeanNamesForType(IBaseMapper.class);
                for (String bean : beanNames) {
                    Class<?> clazz = applicationContext.getBean(bean).getClass();
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> inter : interfaces) {
                        Method[] methods = inter.getMethods();
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(Converter.class)) {
                                if (!MAPPER_INSTANCE_MAP.containsKey(clazz.hashCode())) {
                                    for (IBaseMapper baseMapper : MAPPER_INSTANCES) {
                                        if(baseMapper.getClass().hashCode() == clazz.hashCode()){
                                            MAPPER_INSTANCE_MAP.put(clazz.hashCode(), baseMapper);
                                            break;
                                        }
                                    }
                                }
                                Class<?>[] parameterTypes = method.getParameterTypes();
                                Object[] parameters = getParameters(parameterTypes);
                                IBaseMapper mapper = MAPPER_INSTANCE_MAP.get(clazz.hashCode());
                                method.invoke(mapper, parameters);
                            }
                        }
                    }
                }
            }

            // Get key-values by language type, temporary value of chinese
            Map map = new HashMap();
            map.put("type", language);
            List<KeyValuePojo> keyValuePojos = keyValueMapper.selectKeyValue(map);
            if (keyValuePojos != null && keyValuePojos.size() > 0) {
                for (KeyValuePojo kv : keyValuePojos) {
//                    addEnum(ENUM_TYPE_MAP.get(kv.getEnumName()), kv.getEnumName(), kv.getKey(), kv.getValue());
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Something of %s occurred during the initialization of %s, which can be neglected", e.getMessage(), REDIS_KEY));
        }
    }

    private static void addEnum(Class enumClass, String enumName, String key, String value) {
        EnumUtils.addEnum(enumClass, enumName, new Class<?>[]{String.class, String.class}, new Object[]{key, value});
    }

    private void initDaoInstances(){
        MAPPER_INSTANCES.add(userMapper);
    }

    private Object[] getParameters(Class<?>[] classes){

        Object[] parameters = new Object[classes.length];
        for (int i = 0; i < classes.length; i++) {
            parameters[i] = class2Object(classes[0]);
        }

        return parameters;
    }

    private Object class2Object(Class clazz){

        if (clazz == Map.class) {
            return new HashMap<>();
        }
        if (clazz == List.class) {
            List list = new ArrayList();
            list.add("");
            return list;
        }
        if (clazz == int.class) {
            return 0;
        }
        if (clazz == String.class) {
            return "";
        }
        if (clazz == boolean.class) {
            return false;
        }
        if (clazz == double.class) {
            return 0.0;
        }
        if (clazz == float.class) {
            return 0.0;
        }
        if (clazz == long.class || clazz == Long.class) {
            return 0;
        }
        if (clazz == short.class || clazz == Short.class) {
            return 0;
        }
        if (clazz == byte.class || clazz == Byte.class) {
            return 0;
        }
        if (clazz == char.class || clazz == Character.class) {
            return ' ';
        }

        Object instance = null;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }
}
