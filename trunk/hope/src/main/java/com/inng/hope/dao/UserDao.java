package com.inng.hope.dao;

import com.inng.hope.base.BaseMapper;
import com.inng.hope.entity.User;
import com.inng.hope.framework.mybatis.annotation.Table;


@Table("user")
public interface UserDao extends BaseMapper<User> {



}
