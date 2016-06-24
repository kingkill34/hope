package com.duowan.hope.test.dao;

import java.util.List;

import com.duowan.hope.BaseMapper;
import com.duowan.hope.mybatis.annotation.Table;
import com.duowan.hope.test.entity.User;
import com.duowan.hope.test.entity.UserName;

@Table(UserName.class)
public interface UserNameDao extends BaseMapper<UserName> {

	
	void test(List<User> user);
}
