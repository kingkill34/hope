package com.inng.hope.services;

import java.util.List;

import com.inng.hope.entity.TestUserDetail;

public interface TestUserDetailServices {

	List<TestUserDetail> getList();
	
	Integer add(String userName,String sex,String age,String phone,String addDate);
}
