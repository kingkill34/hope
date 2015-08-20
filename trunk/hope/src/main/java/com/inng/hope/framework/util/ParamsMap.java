package com.inng.hope.framework.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ParamsMap {

	private Map<String, Object> params = new HashMap<String, Object>();
	
	
	

	public ParamsMap(){}
	
	public ParamsMap(String key, String operation, Object value){
		put(key, operation, value);
	}
	
	public ParamsMap(Object entity,String key, String operation, Object value){
		getObjectValue(entity);
		put(key, operation, value);
	}
	
	public ParamsMap(Object entity){
		getObjectValue(entity);
	}
	
	public void putParams(String key, Object value) {
		params.put(key, value);
	}
	
	public void put(String key, String operation, Object value) {
		params.put("operation" + key, operation);
		params.put("where"+key, value);
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
	
	public void setPage(int currentPage,int size){
		params.put("pageStart", currentPage-1 * size);
		params.put("pageEnd", currentPage * size);
	}
	
	public void getObjectValue(Object entity){
		Field[] fields = entity.getClass().getDeclaredFields();
		for(Field field:fields){
			try {
				putParams(field.getName(), field.get(entity));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
