package com.inng.hope.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inng.hope.entity.User;

public interface UserDao {

	/**
	 * 获取用户集合
	 * 
	 * @param pagination
	 * @param pageSize
	 * @return
	 */
	List<User> getList(@Param("pagination") Integer pagination, @Param("pageSize") Integer pageSize);

	/**
	 * 计算用户总数
	 * 
	 * @return
	 */
	Integer count();

	/**
	 * 增加用户
	 * 
	 * @param user
	 * @return
	 */
	Integer addUser(User user);

	User login(@Param("loginName") String loginName);

	Integer changePassword(@Param("id") Integer id, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);

	Integer delUserById(Integer userId);
}
