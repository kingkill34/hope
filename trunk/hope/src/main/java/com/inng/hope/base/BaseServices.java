package com.inng.hope.base;


public interface BaseServices<T> {

	
	Integer insert(T t);
	
	T getById(Integer id);
}
