package com.yxt.crud.yanson;

import com.yxt.crud.exception.InvalidJsonKeyValueFormatException;
import com.yxt.crud.utils.CollectionUtils;
import com.yxt.crud.utils.PatternUtils;
import com.yxt.crud.utils.StringUtils;
import com.yxt.crud.utils.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonObject extends HashMap<String, Object> {

	private static final long serialVersionUID = 4560188633954957114L;

	private static final boolean SET_ON_NONNULL = true;
	private static final String MAGIC = "luxkui";

	public static Object parseObject(String jsonStr) {

		JsonObject jsonObject = new JsonObject();
		StringBuilder sb = new StringBuilder();
		boolean isArray = false;

		String json = jsonStr.trim();
		if (json.startsWith("[") && json.endsWith("]")) {
			isArray = true;
		}
		sb.append("\"").append(MAGIC).append("\"").append(":").append(json);

		try {
			generateObject(jsonObject, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isArray == false ? (JsonObject) jsonObject.get(MAGIC) : getJsonArray(jsonObject);
	}

	private static JsonArray getJsonArray(JsonObject jsonObject) {

		JsonArray jsonArray = (JsonArray) jsonObject.get(MAGIC);
		if (!CollectionUtils.isEmpty(jsonArray)) {
			JsonArray array = new JsonArray(jsonArray.size());
			for (Object obj : jsonArray) {
				JsonObject temp = (JsonObject) obj;
				array.add(temp.get(MAGIC));
			}
			return array;
		} else {
			return new JsonArray(0);
		}

	}

	private static void generateObject(JsonObject jsonObject, String jsonStr) throws Exception {

		if (!StringUtils.isEmpty(jsonStr) && !"isArrayEmptyOrSeparatedByComma".equals(jsonStr)) {

			String kvStr = jsonStr.trim();
			int separator = kvStr.indexOf(":");
			if (-1 == separator) {
				throw new InvalidJsonKeyValueFormatException(String.format("expect key:value, but found %s", kvStr));
			}

			String keyStr = kvStr.substring(0, separator);
			String currKey = TypeUtils.getKey(keyStr);
			String valueStr = kvStr.substring(separator + 1).trim();

			// Object data
			if (valueStr.startsWith("{") && valueStr.endsWith("}")) {

				JsonObject currObject = (JsonObject) jsonObject.get(currKey);
				if (null == currObject) {
					currObject = new JsonObject();
					jsonObject.put(currKey, currObject);
				}

				valueStr = valueStr.substring(1, valueStr.length() - 1);
				List<String> keyValues = TypeUtils.formatKeyValues(valueStr);
				for (String keyValue : keyValues) {

					generateObject(currObject, keyValue);
				}

			// Array data
			} else if (valueStr.startsWith("[") && valueStr.endsWith("]")) {

				List<String> keyValues = TypeUtils.formatKeyValues(valueStr);

				if (!CollectionUtils.isEmpty(keyValues)) {

					if ("isArrayEmptyOrSeparatedByComma".equals(keyValues.get(0))) {

						jsonObject.put(currKey, TypeUtils.getValue(keyValues.get(1)));
					} else {

						JsonArray currArray = (JsonArray) jsonObject.get(currKey);
						if (CollectionUtils.isEmpty(currArray)) {
							currArray = new JsonArray(keyValues.size());
							jsonObject.put(currKey, currArray);
						}

						for (int i = 0; i < keyValues.size(); i++) {

							currArray.add(new JsonObject());
							StringBuilder arrayObject = new StringBuilder();
							arrayObject.append("\"").append(currKey).append("\"").append(":").append(keyValues.get(i));
							generateObject((JsonObject) currArray.get(i), arrayObject.toString());
						}
					}
				}

				// Non object data
			} else {
				jsonObject.put(currKey, TypeUtils.getValue(valueStr));
			}
		} else {
			return;
		}
	}

	public <T> T toJavaObject(String jsonStr, Class<T> clazz) throws Exception {
		return ((JsonObject) parseObject(jsonStr)).toJavaObject0(this, clazz, "");
	}

	@SuppressWarnings("unchecked")
	public <T> T toJavaObject0(Object object, Class<T> clazz, String parent) throws Exception {

		Object instance = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getDeclaredMethods();

		if (object instanceof JsonObject) {

			JsonObject jsonObject = (JsonObject) object;
			if (!StringUtils.isEmpty(parent)) {
				jsonObject = (JsonObject) jsonObject.get(parent);
			}
			for (Field field : fields) {

				Class<?> type = field.getType();
				for (Entry<String, Object> entry : jsonObject.entrySet()) {

					String key = entry.getKey();
					Object value = entry.getValue();
					for (Method method : methods) {

						if (TypeUtils.isFieldMatched(field, method, key)) {
							method.setAccessible(true);
							if (TypeUtils.isElementType(type)) {
								Class<?>[] parameterTypes = method.getParameterTypes();
								method.invoke(instance, TypeUtils.cast2Element(value, parameterTypes[0]));
							} else if (TypeUtils.isCollectionType(type)){
								JsonArray jsonArray = (JsonArray)jsonObject.get(key);
								int size = jsonArray.size();

								Class<?> classType = null;
								Type genericType = field.getGenericType();
								if (genericType instanceof ParameterizedType) {
									ParameterizedType parameterizedType = (ParameterizedType) genericType;
									classType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
								}

								Object o = TypeUtils.cast2Collection(value, type, size);
								if (o instanceof List) {
									List list = (List) o;
									for (Object temp : jsonArray) {
										list.add(toJavaObject0(temp, classType, key));
									}
									method.invoke(instance, list);
								} else if (o instanceof Map) {
									Map map = (Map) o;
									for (Object temp : jsonArray) {
										map.putAll((Map) toJavaObject0(temp, classType, key));
									}
									method.invoke(instance, map);
								}
							} else if (TypeUtils.isArrayType(type)) {
								Object o = jsonObject.get(key);
								Object[] objects = (Object[]) o;
								int size = objects.length;
								if (o instanceof boolean[] || o instanceof Boolean[]) {
									Boolean[] booleans = (Boolean[]) o;
									for (int i = 0; i < size; ++i) {
										booleans[i] = (Boolean) objects[i];
									}
									method.invoke(instance, new Object[]{booleans});
								} else if (o instanceof int[] || o instanceof Integer[]) {
									Integer[] integers = (Integer[]) o;
									for (int i = 0; i < size; ++i) {
										integers[i] = (Integer) objects[i];
									}
									method.invoke(instance, new Object[]{integers});
								}  else if (o instanceof short[] || o instanceof Short[]) {
									Short[] shorts = (Short[]) o;
									for (int i = 0; i < size; ++i) {
										shorts[i] = (Short) objects[i];
									}
									method.invoke(instance, new Object[]{shorts});
								} else if (o instanceof long[] || o instanceof Long[]) {
									Long[] longs = (Long[]) o;
									for (int i = 0; i < size; ++i) {
										longs[i] = (Long) objects[i];
									}
									method.invoke(instance, new Object[]{longs});
								} else if (o instanceof BigInteger[]) {
									BigInteger[] bigIntegers = (BigInteger[]) o;
									for (int i = 0; i < size; ++i) {
										bigIntegers[i] = (BigInteger) objects[i];
									}
									method.invoke(instance, new Object[]{bigIntegers});
								} else if (o instanceof float[] || o instanceof Float[]) {
									Float[] floats = (Float[]) o;
									for (int i = 0; i < size; ++i) {
										floats[i] = (Float) objects[i];
									}
									method.invoke(instance, new Object[]{floats});
								} else if (o instanceof double[] || o instanceof Double[]) {
									Double[] doubles = (Double[]) o;
									for (int i = 0; i < size; ++i) {
										doubles[i] = (Double) objects[i];
									}
									method.invoke(instance, new Object[]{doubles});
								} else if (o instanceof BigDecimal[]) {
									BigDecimal[] bigDecimals = (BigDecimal[]) o;
									for (int i = 0; i < size; ++i) {
										bigDecimals[i] = (BigDecimal) objects[i];
									}
									method.invoke(instance, new Object[]{bigDecimals});
								} else if (o instanceof byte[] || o instanceof Byte[]) {
									Byte[] bytes = (Byte[]) o;
									for (int i = 0; i < size; ++i) {
										bytes[i] = (Byte) objects[i];
									}
									method.invoke(instance, new Object[]{bytes});
								} else if (o instanceof char[] || o instanceof Character[]) {
									Character[] characters = (Character[]) o;
									for (int i = 0; i < size; ++i) {
										characters[i] = (Character) objects[i];
									}
									method.invoke(instance, new Object[]{characters});
								} else {
									method.invoke(instance, new Object[]{objects});
								}
							}
							break;
						}
					}
				}
			}
		}

		return (T) instance;
	}

	@Override
	public String toString() {
		return this.toJsonString();
	}

	public String toJsonString() {

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		recursive2String(sb, this, false);
		sb.append("}");
		String str = PatternUtils.commaRightCurlyBracket(sb.toString(), "}");
		return PatternUtils.commaRightSquareBracket(str, "]");

	}

	@SuppressWarnings("unchecked")
	private void recursive2String(StringBuilder sb, Object object, boolean isList) {

		if (object instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) object;
			for (Entry<String, Object> entry : map.entrySet()) {
				Object value = entry.getValue();
				if (value instanceof Map) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append("{");
					recursive2String(sb, value, false);
					sb.append("}");
				} else if (value instanceof String) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append((String) value);
				} else if (value instanceof Integer) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof Short) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof Long) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof BigInteger) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof Float) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof Double) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof BigDecimal) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof Boolean) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (null == value) {
					if (SET_ON_NONNULL) {
						if (!isList) {
							sb.append("\"" + entry.getKey() + "\"");
							sb.append(":");
						}
					} else {

					}
				} else if (value instanceof List) {
					sb.append("\"" + entry.getKey() + "\"");
					sb.append(":");
					sb.append("[");
					List<?> list = (List<?>) value;
					for (Object o : list) {
						recursive2String(sb, o, true);
					}
					sb.append("]");
				}
				sb.append(",");
			}
		} else {
			return;
		}
	}

}
