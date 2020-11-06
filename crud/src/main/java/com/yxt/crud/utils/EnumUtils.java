package com.yxt.crud.utils;


import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.*;
import java.util.*;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/3/17
 */
public class EnumUtils {

    private EnumUtils() {

    }

    public static Map getEnumMap(Class clazz) throws Exception{

        Map map = new HashMap();
        Object[] objects = clazz.getEnumConstants();

        Method keyMethod = clazz.getMethod("getKey");
        Method valueMethod = clazz.getMethod("getValue");
        for (Object obj : objects) {

            map.put(keyMethod.invoke(obj), valueMethod.invoke(obj));
        }

        return map;
    }

    private static ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();

    private static void setFailsafeFieldValue(Field field, Object target, Object value) throws NoSuchFieldException,
            IllegalAccessException {

        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        int modifiers = modifiersField.getInt(field);
        modifiers &= ~Modifier.FINAL;
        modifiersField.setInt(field, modifiers);
        FieldAccessor fa = reflectionFactory.newFieldAccessor(field, false);
        fa.set(target, value);
    }

    private static void blankField(Class<?> enumClass, String fieldName) throws NoSuchFieldException,
            IllegalAccessException {
        for (Field field : Class.class.getDeclaredFields()) {
            if (field.getName().contains(fieldName)) {
                AccessibleObject.setAccessible(new Field[] { field }, true);
                setFailsafeFieldValue(field, enumClass, null);
                break;
            }
        }
    }

    private static void cleanEnumCache(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException {
        blankField(enumClass, "enumConstantDirectory"); // Sun (Oracle) JDK 1.5/6
        blankField(enumClass, "enumConstants"); // IBM JDK
    }

    private static ConstructorAccessor getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes)
            throws NoSuchMethodException {
        Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
        parameterTypes[0] = String.class;
        parameterTypes[1] = int.class;
        System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
        return reflectionFactory.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
    }

    private static Object makeEnum(Class<?> enumClass, String value, int ordinal, Class<?>[] additionalTypes,
                                   Object[] additionalValues) throws Exception {
        Object[] params = new Object[additionalValues.length + 2];
        params[0] = value;
        params[1] = Integer.valueOf(ordinal);
        System.arraycopy(additionalValues, 0, params, 2, additionalValues.length);
        return enumClass.cast(getConstructorAccessor(enumClass, additionalTypes).newInstance(params));
    }

    public static <T extends Enum<?>> void addEnum(Class<T> enumType, String enumName, Class<?>[] additionalTypes, Object[] additionalValues) {

        if (!Enum.class.isAssignableFrom(enumType)) {
            throw new RuntimeException("class " + enumType + " is not an instance of Enum");
        }

        Field valuesField = null;
        Field[] fields = enumType.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().contains("$VALUES")) {
                valuesField = field;
                break;
            }
        }
        AccessibleObject.setAccessible(new Field[] { valuesField }, true);

        try {

            T[] previousValues = (T[]) valuesField.get(enumType);
            List<T> values = new ArrayList<T>(Arrays.asList(previousValues));

            T newValue = (T) makeEnum(enumType, enumName, values.size(), additionalTypes, additionalValues);

            values.add(newValue);

            setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));

            cleanEnumCache(enumType);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
