package com.inng.hope.services;

import java.util.List;

import com.inng.hope.base.BaseServices;
import com.inng.hope.entity.User;

public interface UserDaoServices extends BaseServices<User> {


	User login(String loginName, String loginPassword);

	List<User> getList(String loginName, String loginPassword);

	
}
