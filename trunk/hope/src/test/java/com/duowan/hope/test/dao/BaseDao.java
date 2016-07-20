package com.duowan.hope.test.dao;

import java.util.List;

import com.duowan.hope.mybatis.annotation.HopeSelect;
import com.duowan.hope.mybatis.page.HopePage;

public interface BaseDao<T> {

	@HopeSelect
	T getById(Integer idTest);

	@HopeSelect
	List<T> getList(String name);

	@HopeSelect
	HopePage<T> getListByName2(String name, int pageSize, int pageNo);

}
