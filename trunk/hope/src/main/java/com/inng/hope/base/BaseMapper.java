package com.inng.hope.base;

import java.util.List;
import java.util.Map;

import com.inng.hope.framework.util.ParamsMap;

public interface BaseMapper<T> {

	public T get(ParamsMap paramsMap);
	
	public List<T> getList(ParamsMap paramsMap);
	
	public Integer insert(T t);
	
	public Integer update(ParamsMap paramsMap);
	
	public Integer delete(ParamsMap paramsMap);
}
