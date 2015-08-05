package com.inng.hope.base;

public interface BaseMapper<T> {

	public T get(Integer id);
	
	public Integer insert(T t);
}
