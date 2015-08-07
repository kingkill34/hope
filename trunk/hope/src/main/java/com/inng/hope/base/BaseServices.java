package com.inng.hope.base;



public interface BaseServices<T> {

	Integer insert(T t);
	
	T getById(Integer id);
	
	Integer update(T t);
	
	Integer delById(Integer id);
	
}
