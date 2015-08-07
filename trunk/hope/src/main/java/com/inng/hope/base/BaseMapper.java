package com.inng.hope.base;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {

	public T get(Map<String, Object> params);
	
	public List<T> getList(Map<String, Object> params);
	
	public Integer insert(T t);
	
	public Integer update(T t);
	
	public Integer delete(Map<String, Object> params);
}
