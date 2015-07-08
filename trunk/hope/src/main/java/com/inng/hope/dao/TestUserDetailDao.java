package com.inng.hope.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inng.hope.entity.TestUserDetail;

public interface TestUserDetailDao {

	
	List<TestUserDetail> getList();
	
	
	Integer add(@Param("userName") String userName,
				@Param("sex") String sex,
				@Param("age") String age,
				@Param("phone") String phone,
				@Param("addDate") String addDate);
}
