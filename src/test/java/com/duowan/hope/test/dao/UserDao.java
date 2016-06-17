package com.duowan.hope.test.dao;

import java.util.List;
import java.util.Map;

import com.duowan.hope.mybatis.annotation.HopeInsert;
import com.duowan.hope.mybatis.annotation.HopeSelect;
import com.duowan.hope.mybatis.annotation.OP;
import com.duowan.hope.mybatis.annotation.Table;
import com.duowan.hope.test.entity.User;

@Table(value = "user", tableSuffix = "gameId")
public interface UserDao {

	// 返回单个ENTITY
	// @HopeSelect
	User getUser(String loginName);

	// 返回list
	// @HopeSelect
	List<User> getUserList(String nickName);

	// 返回Map
	// @HopeSelect
	Map<String, Object> getUserMap(String loginName);

	// 返回Map
	// @HopeSelect("id")
	Map<String, Object> getUserMapParam(String loginName);

	// 返回Map
	// @HopeSelect("id,loginName")
	Map<String, Object> getUserMapParams(String loginName);

	// 返回Map
	// @HopeSelect("id")
	List<Map<String, Object>> getUserListMapParam(String loginName);

	// 返回Map
	// @HopeSelect("id,loginName")
	List<Map<String, Object>> getUserListMapParams(String loginName);

	// 返回Map
	// @HopeSelect
	List<Map<String, Object>> getUserListMap(String nickName);

	// 多参数返回entity
	// @HopeSelect
	User getUserTwoParams(String nickName, String loginName);

	// 多参数返回List
	// @HopeSelect
	List<User> getUserTwoParamsList(String nickName, Integer type);

	// 返回list
	// @HopeSelect(groupBy = "nickName", orderByASC = "id")
	List<User> getUserListGroupBy(String nickName);

	// 返回list
	// @HopeSelect(groupBy = "loginName,id")
	List<User> getUserMapGroupBy(String nickName);

	// @HopeCount(value="nickName")
	Integer getUserCount(String loginName);

	// @HopeCount(value = "nickName", groupBy = "loginPassword")
	Map<String, Object> getUserMapCount(String loginName);

	// @HopeSelect
	List<User> testOP(@OP("!=") Integer id);

	//@HopeSelect
	List<User> testTSN(Integer id, Integer gameId);

	@HopeInsert
	boolean insert(User user);
	
//	@HopeInsert
//	Boolean insertByBoolean(User user);
//	
//	@HopeInsert
//	Integer insertByInteger(User user);
//
//	@HopeInsert
//	Integer insertByInt(User user);

}
