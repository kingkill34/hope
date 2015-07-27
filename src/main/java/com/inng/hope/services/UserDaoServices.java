package com.inng.hope.services;

import javax.servlet.http.HttpSession;

import com.inng.hope.entity.User;
import com.inng.hope.result.ResultObject;

public interface UserDaoServices {

	ResultObject getList(Integer pagination, HttpSession session);

	User login(String loginName, String loginPassword);

	ResultObject changePassword(Integer id, String newPassword, String oldPassword);

	ResultObject delUserById(Integer userId);

	ResultObject addUser(String userName, String password);
	
	void getByid(Integer id);
}
