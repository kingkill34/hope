package com.inng.hope.dao;

import org.apache.ibatis.annotations.Param;

import com.inng.hope.entity.TestUser;

public interface TestUserDao {

	
	TestUser login(@Param("userName")String userName,
				   @Param("password")String password);
	
	Integer getNumber(@Param("userName")String userName);
	
	Integer updateNumber(@Param("number") String number,
						  @Param("userName") String userName);
	
	
	Integer updateFlag(@Param("userName") String userName,
					   @Param("flag")String flag);
}
