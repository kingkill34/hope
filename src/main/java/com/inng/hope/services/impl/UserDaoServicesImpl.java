package com.inng.hope.services.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.inng.hope.dao.UserDao;
import com.inng.hope.entity.User;
import com.inng.hope.result.ResultObject;
import com.inng.hope.result.code.UserPrompt;
import com.inng.hope.services.UserDaoServices;
import com.inng.hope.util.Page;

@Service(value = "userDaoServicesImpl")
public class UserDaoServicesImpl extends ResultObject implements
		UserDaoServices {

	@Resource(name = "userDao")
	private UserDao userDao;

	/**
	 * 获取用户集合                                                                                                                                                              
	 */
	@Override
	public ResultObject getList(Integer pagination,HttpSession session) {
		//分页对象
		Page page = null;
		
		ResultObject resultObject = null;
		try {
			
			if(pagination==1){
				page = new Page(pagination, 2, userDao.count(), 3);
				session.setAttribute("page", page);
			}else{
				page = (Page)session.getAttribute("page");
				page.setChoosePage(pagination);
			}
			
			
			
			
			List<User> userList = userDao.getList((pagination-1)*2,2);
			Assert.notNull(userList);
			resultObject = new ResultObject(UserPrompt.SUCCESS, userList,page);
		} catch (Exception e) {
			resultObject = new ResultObject(UserPrompt.EXCEPTION, e);
		}
		return resultObject;
	}

	@Override
	public ResultObject login() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultObject changePassword(Integer id, String oldPassword,
			String newPassword) {
		ResultObject resultObject = null;
		try {
			int result = userDao.changePassword(id, oldPassword, newPassword);
			if (result == 1) {
				resultObject = new ResultObject(UserPrompt.SUCCESS,UserPrompt.CHANGE_PASSWORD_SUCCESS);
			} else {
				resultObject = new ResultObject(UserPrompt.FAIL,UserPrompt.CHANGE_PASSWORD_ERROR);
			}
		} catch (Exception e) {
			resultObject = new ResultObject(UserPrompt.FAIL, e);
		}
		return resultObject;
	}

	@Override
	public ResultObject delUserById(Integer userId) {
		ResultObject resultObject = null;
		try {
			int result = userDao.delUserById(userId);
			if (result == 1) {
				resultObject = new ResultObject(UserPrompt.SUCCESS,UserPrompt.DEL_USER_SUCCESS);
			} else {
				resultObject = new ResultObject(UserPrompt.FAIL,UserPrompt.DEL_USER_ERROR);
			}
		} catch (Exception e) {
			resultObject = new ResultObject(UserPrompt.FAIL, e);
		}
		return resultObject;
	}

	@Override
	public ResultObject addUser(String userName, String password) {
		User user = new User(userName,password);
		ResultObject resultObject = null;
		try {
			int result = userDao.addUser(user);
			if (result == 1) {
				resultObject = new ResultObject(UserPrompt.SUCCESS,UserPrompt.ADD_USER_SUCCESS);
			} else {
				resultObject = new ResultObject(UserPrompt.FAIL,UserPrompt.ADD_USER_ERROR);
			}
		} catch (Exception e) {
			resultObject = new ResultObject(UserPrompt.FAIL, e);
		}
		return resultObject;
	}
}
