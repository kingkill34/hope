package com.duowan.hope.test.dao;

import com.duowan.hope.BaseMapper;
import com.duowan.hope.mybatis.annotation.Table;
import com.duowan.hope.test.entity.UserName;

@Table("user_name")
public interface UserNameDao extends BaseMapper<UserName> {

}
