package com.inng.hope.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inng.hope.dao.UserDao;
import com.inng.hope.entity.User;
import com.inng.hope.exception.user.UserNotExits;
import com.inng.hope.exception.user.UserOrPasswordError;
import com.inng.hope.framework.util.ParamsMap;
import com.inng.hope.result.ResultObject;
import com.inng.hope.services.UserDaoServices;

@Service(value = "userDaoServicesImpl")
public class UserDaoServicesImpl extends ResultObject implements UserDaoServices {

	@Resource(name = "userDao")
	private UserDao userDao;

	@Override
	public User login(String loginName, String loginPassword) {
		ParamsMap paramsMap = new ParamsMap("loginName", loginName);
		paramsMap.put("loginPassword",loginPassword);
		User user = userDao.get(paramsMap);
		if (user == null) {
			throw new UserNotExits();
		}

		if (!user.getLoginPassword().equals(loginPassword)) {
			throw new UserOrPasswordError();
		}

		return user;
	}

	@Override
	public User getById(Integer id) {
		ParamsMap paramsMap = new ParamsMap("id", id);
		return userDao.get(paramsMap);
	}

	@Override
	public Integer insert(User t) {
		return userDao.insert(t);
	}

	@Override
	public List<User> getList(String loginName, String loginPassword) {
		ParamsMap paramsMap = new ParamsMap("loginName", loginName);
		paramsMap.put("loginPassword", loginPassword);
		return userDao.getList(paramsMap);
	}

	@Override
	public Integer delById(Integer id) {
		ParamsMap params = new ParamsMap("id", id);
		return userDao.delete(params);
	}

	@Override
	public Integer update(User user) {
		ParamsMap paramsMap = new ParamsMap(user, "id", 1);
		return userDao.update(paramsMap);
	}

}
