package com.inng.hope.dao;

import com.inng.hope.base.BaseMapper;
import com.inng.hope.entity.User;

public interface UserDao extends BaseMapper<User> {

	

	User login(String loginName);

}
