package com.duowan.hope.test.dao;

import java.util.List;

import com.duowan.hope.BaseMapper;
import com.duowan.hope.Params;
import com.duowan.hope.mybatis.annotation.Entity;
import com.duowan.hope.test.entity.User;

@Entity(User.class)
public interface UserDao extends BaseMapper<User> {

	List<User> getList(Params params);
}