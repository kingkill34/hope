package com.duowan.hope.test.dao;

import com.duowan.hope.BaseMapper;
import com.duowan.hope.mybatis.annotation.Entity;
import com.duowan.hope.test.entity.UserName;

@Entity(UserName.class)
public interface UserNameDao extends BaseMapper<UserName> {

	Integer testInsert(UserName userName);
}
