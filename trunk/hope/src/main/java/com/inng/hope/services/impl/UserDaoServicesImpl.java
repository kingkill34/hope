package com.inng.hope.services.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.inng.hope.dao.UserDao;
import com.inng.hope.entity.User;
import com.inng.hope.exception.user.UserNotExits;
import com.inng.hope.exception.user.UserOrPasswordError;
import com.inng.hope.result.ResultObject;
import com.inng.hope.services.UserDaoServices;

@Service(value = "userDaoServicesImpl")
public class UserDaoServicesImpl extends ResultObject implements UserDaoServices {

	@Resource(name = "userDao")
	private UserDao userDao;


	@Override
	public User login(String loginName, String loginPassword) {
		User user = userDao.login(loginName);
		if (user == null) {
			throw new UserNotExits();
		}
		
		if(!user.getLoginPassword().equals(loginPassword)){
			throw new UserOrPasswordError();
		}
		
		return user;
	}
	
	
	@Override			
	public User getById(Integer id) {
		return userDao.get(id);
	}


	@Override
	public ResultObject getList(Integer pagination, HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ResultObject changePassword(Integer id, String newPassword, String oldPassword) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ResultObject delUserById(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ResultObject addUser(String userName, String password) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Integer insert(User t) {
		return userDao.insert(t);
	}


	

	
}
