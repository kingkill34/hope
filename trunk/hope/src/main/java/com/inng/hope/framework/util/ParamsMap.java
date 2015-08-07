package com.inng.hope.framework.util;

import java.util.HashMap;
import java.util.Map;

public class ParamsMap {

	private Map<String, Object> params = new HashMap<String, Object>();
	
	

	public ParamsMap(){}
	
	public ParamsMap(String key, String operation, Object value){
		put(key, operation, value);
	}
	
	public void put(String key, String operation, Object value) {
		params.put("operation" + key, operation);
		params.put(key, value);
	}

	public Map<String, Object> getMap() {
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
}
