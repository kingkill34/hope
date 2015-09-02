package com.inng.hope.framework.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ParamsMap {

	private Map<String, Object> params = new HashMap<String, Object>();

	private static final String EQUALS_OPERATION = "=";

	public ParamsMap() {
	}

	public ParamsMap(String key, String operation, Object value) {
		put(key, operation, value);
	}

	public ParamsMap(String key, Object value) {
		put(key, EQUALS_OPERATION, value);
	}

	public ParamsMap(Object entity, String key, String operation, Object value) {
		setObjectValue(entity);
		put(key, operation, value);
	}

	public ParamsMap(Object entity, String key, Object value) {
		setObjectValue(entity);
		put(key, EQUALS_OPERATION, value);
	}

	public ParamsMap(Object entity) {
		setObjectValue(entity);
	}

	public void putParams(String key, Object value) {
		params.put(key, value);
	}

	public void put(String key, String operation, Object value) {
		params.put("operation" + key, operation);
		params.put("where" + key, value);
	}
	
	public void put(String key, Object value) {
		params.put("operation" + key, EQUALS_OPERATION);
		params.put("where" + key, value);
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setOrderBy(String orderBy) {
		params.put("order by", orderBy);
	}

	public void setGroupBy(String groupBy) {
		params.put("group by", groupBy);
	}

	public void setPage(int currentPage, int size) {
		params.put("pageStart", currentPage - 1 * size);
		params.put("pageEnd", currentPage * size);
	}

	public void setObjectValue(Object entity) {
		Field[] fields = entity.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				putParams(field.getName(), field.get(entity));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
