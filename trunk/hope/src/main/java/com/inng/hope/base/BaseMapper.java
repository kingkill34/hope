package com.inng.hope.base;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {

	public T get(Map<String, Object> condtions);
	
	public List<T> getList(Map<String, Object> condtions);
	
	public Integer insert(T t);
	
	public Integer update(Map<String, Object> map);
	
	public Integer delete(Map<String, Object> condtions);
}
