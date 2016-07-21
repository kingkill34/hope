package com.duowan.hope.test.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.duowan.hope.mybatis.annotation.HopeCount;
import com.duowan.hope.mybatis.annotation.HopeDelete;
import com.duowan.hope.mybatis.annotation.HopeInsert;
import com.duowan.hope.mybatis.annotation.HopeSelect;
import com.duowan.hope.mybatis.annotation.HopeUpdate;
import com.duowan.hope.mybatis.annotation.OP;
import com.duowan.hope.mybatis.annotation.Table;
import com.duowan.hope.test.entity.UserName;

@Table(value = UserName.class, page = 20)
public interface UserNameDao {
	
	
	@HopeSelect
	List<UserName> getId(@OP(">")Integer id,String name);

	@HopeInsert
	Integer insertInteger(UserName userName);

	@HopeInsert
	Boolean insertBoolean(UserName userName);

	@HopeInsert(returnPrimaryKey = true)
	Integer insertParmaryKey(UserName userName);

	@HopeInsert
	Integer insertParam(String name, Date datetime, Integer age);

	@HopeInsert
	Integer insertIntegerList(List<UserName> list);

	@HopeInsert
	Integer insertIntegerArray(UserName[] userNames);

	@HopeDelete
	Integer deleteByName(String name);

	@HopeDelete
	Integer deleteByLike(@OP("like") String name);

	@HopeDelete
	Integer deleteAll();

	@HopeSelect
	UserName getUserNameById(Integer id);

	@HopeSelect
	List<UserName> getUserNameByName(@OP("like") String name);

	@HopeSelect
	TreeMap<String, Object> getUserNameByIdMap(Integer id);

	@HopeSelect
	List<Map> getUserNameByNameMap(@OP("like") String name);

	@HopeSelect("id,name,money")
	List<Map> getByNameMap(@OP("like") String name);

	@HopeSelect("id,name,money")
	List<UserName> getByNameList(@OP("like") String name);

	@HopeSelect("id,isFirst,datetime")
	UserName getById(Integer id);

	@HopeSelect(orderByASC = "id")
	List<UserName> getByNameOrderByASC(String name);

	@HopeSelect(orderByDESC = "id")
	List<UserName> getByNameOrderByDESC(String name);

	@HopeSelect(groupBy = "name")
	List<UserName> getByNameGroupBy(String name);

	@HopeSelect
	List<UserName> getIsNull(@OP(isNull = true) String nums);

	@HopeSelect
	List<UserName> getIsNotNull(@OP(isNotNull = true) String nums);

	@HopeSelect
	List<UserName> getIsNullByName(@OP(isNull = true) String nums, String name);

	@HopeCount
	Integer count();

	@HopeCount("name")
	Integer countName(String name);

	@HopeCount(value = "name", distinct = true)
	Integer countDistinctName(String name);
	
	@HopeCount(value = "isFirst", distinct = true,groupBy="id")
	List<Map> countGroupbyName(boolean isFirst);

	@HopeUpdate("id")
	int update(UserName userName);

	@HopeUpdate("id,name")
	int updateParam(UserName userName);

	@HopeUpdate
	boolean updateById(UserName userName, @OP Integer id);

	@HopeUpdate
	int updateSingleParam(@OP(isUpdate = true) String name, int id);

}
