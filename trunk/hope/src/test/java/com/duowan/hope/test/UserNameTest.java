package com.duowan.hope.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.duowan.hope.mybatis.annotation.HopeSelect;
import com.duowan.hope.mybatis.annotation.HopeUpdate;
import com.duowan.hope.mybatis.annotation.OP;
import com.duowan.hope.test.dao.UserNameDao;
import com.duowan.hope.test.entity.UserName;

@ContextConfiguration(locations = "classpath*:/*context.xml")
public class UserNameTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private UserNameDao userNameDao;

	@Test
	public void deleteAll() {
		userNameDao.deleteAll();
	}

	/**
	 * 测试增加数据返回Integer值
	 */
	@Test
	public void insertByInteger() {
		// 增加
		UserName userName = new UserName();
		userName.initSingleTestData();
		Integer result = userNameDao.insertInteger(userName);
		Assert.isTrue(result == 1, "插入结果:" + result);

		// 删除
		result = userNameDao.deleteByName("frankie");
		Assert.isTrue(result == 1, "删除结果:" + result);
	}

	/**
	 * 测试增加数据返回Integer值
	 */
	@Test
	public void insertParmaryKey() {
		// 增加
		UserName userName = new UserName();
		userName.initSingleTestData();
		Integer result = userNameDao.insertParmaryKey(userName);
		Assert.isTrue(result == 1, "插入结果:" + result);

		Assert.isTrue(userName.getId() > 1, "ID:" + result);

		// 删除
		result = userNameDao.deleteByName("frankie");
		Assert.isTrue(result > 0, "删除结果:" + result);
	}

	/**
	 * 测试增加数据返回Boolean值
	 */
	@Test
	public void insertByBoolean() {
		// 增加
		UserName userName = new UserName();
		userName.initSingleTestData();
		boolean result = userNameDao.insertBoolean(userName);
		Assert.isTrue(result, "插入结果:" + result);

		// 删除
		Integer deleteResult = userNameDao.deleteByName("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 测试添加普通参数
	 */
	@Test
	public void insertByParam() {
		// 增加
		Integer result = userNameDao.insertParam("frankie", new Date(), 30);
		Assert.isTrue(result == 1, "插入结果:" + result);

		// 删除
		Integer deleteResult = userNameDao.deleteByName("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 测试添加集合List
	 */
	@Test
	public void insertIntegerList() {
		List<UserName> list = new ArrayList<UserName>();
		for (int i = 1; i < 10; i++) {
			UserName userName = new UserName();
			userName.initListTestData(i);
			list.add(userName);
		}

		// 增加
		Integer result = userNameDao.insertIntegerList(list);
		Assert.isTrue(result == 9, "插入结果:" + result);

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 测试添加数组
	 */
	@Test
	public void insertIntegerArray() {
		UserName[] userNames = new UserName[10];
		for (int i = 0; i < 10; i++) {
			UserName userName = new UserName();
			userName.initListTestData(i);
			userNames[i] = userName;
		}

		// 增加
		Integer result = userNameDao.insertIntegerArray(userNames);
		Assert.isTrue(result == 10, "插入结果:" + result);

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 测试根据ID获取单条数据
	 */
	@Test
	public void getUserNameById() {
		// 增加
		UserName userName = new UserName();
		userName.initSingleTestData();
		Integer result = userNameDao.insertParmaryKey(userName);
		Assert.isTrue(result == 1, "插入结果:" + result);

		Integer id = userName.getId();

		UserName userName2 = userNameDao.getUserNameById(id);
		Assert.isTrue(id.intValue() == userName2.getId().intValue(), "插入ID和或者ID比较:" + (id.intValue() == userName2.getId().intValue()));

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 测试根据ID获取多条数据
	 */
	@Test
	public void getUserNameByName() {
		// 初始化数据
		UserName[] userNames = new UserName[10];
		for (int i = 0; i < 10; i++) {
			UserName userName = new UserName();
			userName.initListTestData(i);
			userNames[i] = userName;
		}

		// 增加
		Integer result = userNameDao.insertIntegerArray(userNames);
		Assert.isTrue(result == 10, "插入结果:" + result);

		List<UserName> list = userNameDao.getUserNameByName("frankie");

		Assert.isTrue(list.size() == 10, "获取结果:" + (list.size() == 10));

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 测试根据ID获取单条数据,返回MAP
	 */
	@Test
	public void getUserNameByIdMap() {
		// 增加
		UserName userName = new UserName();
		userName.initSingleTestData();
		Integer result = userNameDao.insertParmaryKey(userName);
		Assert.isTrue(result == 1, "插入结果:" + result);

		Integer id = userName.getId();

		Map<String, Object> map = userNameDao.getUserNameByIdMap(id);
		Assert.isTrue(String.valueOf(id).equals(map.get("id").toString()), "插入ID和或者ID比较:" + (String.valueOf(id).equals(map.get("id").toString())));

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 根据name 模糊查询获取数据,返回List<MAP>
	 */
	@Test
	public void getUserNameByNameMap() {
		// 初始化数据
		UserName[] userNames = new UserName[10];
		for (int i = 0; i < 10; i++) {
			UserName userName = new UserName();
			userName.initListTestData(i);
			userNames[i] = userName;
		}

		// 增加
		Integer result = userNameDao.insertIntegerArray(userNames);
		Assert.isTrue(result == 10, "插入结果:" + result);

		List<Map> list = userNameDao.getUserNameByNameMap("frankie");

		Assert.isTrue(list.size() == 10, "获取结果:" + (list.size() == 10));

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 根据name 模糊查询获取输入字段数据,返回List<MAP>
	 */
	@Test
	public void getByNameMap() {
		// 初始化数据
		UserName[] userNames = new UserName[10];
		for (int i = 0; i < 10; i++) {
			UserName userName = new UserName();
			userName.initListTestData(i);
			userNames[i] = userName;
		}

		// 增加
		Integer result = userNameDao.insertIntegerArray(userNames);
		Assert.isTrue(result == 10, "插入结果:" + result);

		List<Map> list = userNameDao.getByNameMap("frankie");

		for (Map map : list) {
			Assert.isTrue(!StringUtils.isEmpty(map.get("id")), "用户ID为空");
			Assert.isTrue(!StringUtils.isEmpty(map.get("name")), "用户名称为空");
			Assert.isTrue(!StringUtils.isEmpty(map.get("money")), "用户金钱为空");
			Assert.isTrue(StringUtils.isEmpty(map.get("time")), "用户时间不为空");
		}

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);

	}

	/**
	 * 根据name 模糊查询获取输入字段数据,返回List<UserName>
	 */
	@Test
	public void getByNameList() {
		// 初始化数据
		UserName[] userNames = new UserName[10];
		for (int i = 0; i < 10; i++) {
			UserName userName = new UserName();
			userName.initListTestData(i);
			userNames[i] = userName;
		}

		// 增加
		Integer result = userNameDao.insertIntegerArray(userNames);
		Assert.isTrue(result == 10, "插入结果:" + result);

		List<UserName> list = userNameDao.getByNameList("frankie");

		for (UserName userName : list) {
			Assert.isTrue(!StringUtils.isEmpty(userName.getId()), "用户ID为空");
			Assert.isTrue(!StringUtils.isEmpty(userName.getName()), "用户名称为空");
			Assert.isTrue(!StringUtils.isEmpty(userName.getMoney()), "用户金钱为空");
			Assert.isTrue(StringUtils.isEmpty(userName.getTime()), "用户时间不为空");
		}

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 根据name 模糊查询获取输入字段数据,返回List<UserName>
	 */
	@Test
	public void getByName() {

		// 增加
		UserName userName = new UserName();
		userName.initSingleTestData();
		Integer result = userNameDao.insertParmaryKey(userName);
		Assert.isTrue(result == 1, "插入结果:" + result);

		Integer id = userName.getId();

		UserName userName2 = userNameDao.getById(id);

		Assert.isTrue(!StringUtils.isEmpty(userName2.getId()), "用户ID为空");
		Assert.isTrue(!StringUtils.isEmpty(userName2.getIsFirst()), "用户名称为空");
		Assert.isTrue(!StringUtils.isEmpty(userName2.getDatetime()), "用户金钱为空");
		Assert.isTrue(StringUtils.isEmpty(userName2.getTime()), "用户时间不为空");

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);

	}

	/**
	 * 测试ORDER_BY_ASC
	 */
	@Test
	public void getByNameOrderByASC() {

		// 初始化数据
		UserName[] userNames = new UserName[10];
		for (int i = 0; i < 10; i++) {
			UserName userName = new UserName();
			userName.initListSameTestData(i);
			userNames[i] = userName;
		}

		// 增加
		Integer result = userNameDao.insertIntegerArray(userNames);
		Assert.isTrue(result == 10, "插入结果:" + result);

		List<UserName> list = userNameDao.getByNameOrderByASC("frankie");
		Integer id = -1;
		for (UserName userName : list) {
			Assert.isTrue(userName.getName().equals("frankie"), "结果:" + userName.getName().equals("frankie"));
			Assert.isTrue(userName.getId().intValue() > id.intValue(), "ID比对结果:" + (userName.getId().intValue() > id.intValue()));
			if (id == -1) {
				id = userName.getId();
			}
		}
		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 测试ORDER_BY_DESC
	 */
	@Test
	public void getByNameOrderByDESC() {

		// 初始化数据
		UserName[] userNames = new UserName[10];
		for (int i = 0; i < 10; i++) {
			UserName userName = new UserName();
			userName.initListSameTestData(i);
			userNames[i] = userName;
		}

		// 增加
		Integer result = userNameDao.insertIntegerArray(userNames);
		Assert.isTrue(result == 10, "插入结果:" + result);

		List<UserName> list = userNameDao.getByNameOrderByDESC("frankie");
		Integer id = 9999;
		for (UserName userName : list) {
			Assert.isTrue(userName.getName().equals("frankie"), "结果:" + userName.getName().equals("frankie"));
			Assert.isTrue(userName.getId().intValue() < id.intValue(), "ID比对结果:" + (userName.getId().intValue() < id.intValue()));
			if (id == 9999) {
				id = userName.getId();
			}
		}
		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 测试GROUP BY
	 */
	@Test
	public void getByNameGroupBy() {

		// 初始化数据
		UserName[] userNames = new UserName[10];
		for (int i = 0; i < 10; i++) {
			UserName userName = new UserName();
			userName.initListSameTestData(i);
			userNames[i] = userName;
		}

		// 增加
		Integer result = userNameDao.insertIntegerArray(userNames);
		Assert.isTrue(result == 10, "插入结果:" + result);

		List<UserName> list = userNameDao.getByNameGroupBy("frankie");

		Assert.isTrue(list.size() == 1, "结果:" + (list.size() == 1));

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 测试参数ISNULL
	 */
	@Test
	public void getIsNull() {

		// 初始化数据
		UserName[] userNames = new UserName[10];
		for (int i = 0; i < 10; i++) {
			UserName userName = new UserName();
			userName.initListSameTestData(i);
			userNames[i] = userName;
		}

		// 增加
		Integer result = userNameDao.insertIntegerArray(userNames);
		Assert.isTrue(result == 10, "插入结果:" + result);

		List<UserName> list = userNameDao.getIsNull(null);

		Assert.isTrue(list.size() == 10, "结果:" + (list.size() == 10));

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 测试参数ISNULL
	 */
	@Test
	public void getIsNotNull() {

		// 初始化数据
		UserName[] userNames = new UserName[10];
		for (int i = 0; i < 10; i++) {
			UserName userName = new UserName();
			userName.initListSameTestData(i);
			userNames[i] = userName;
		}

		// 增加
		Integer result = userNameDao.insertIntegerArray(userNames);
		Assert.isTrue(result == 10, "插入结果:" + result);

		List<UserName> list = userNameDao.getIsNotNull(null);

		Assert.isTrue(list.size() == 0, "结果:" + (list.size() == 0));

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 测试参数ISNULL
	 */
	@Test
	public void getIsNullByName() {

		// 初始化数据
		UserName[] userNames = new UserName[10];
		for (int i = 0; i < 10; i++) {
			UserName userName = new UserName();
			userName.initListTestData(i);
			userNames[i] = userName;
		}

		// 增加
		Integer result = userNameDao.insertIntegerArray(userNames);
		Assert.isTrue(result == 10, "插入结果:" + result);

		List<UserName> list = userNameDao.getIsNullByName(null, "frankie1");

		Assert.isTrue(list.size() == 1, "结果:" + (list.size() == 1));

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 根据ID更新
	 */
	@Test
	public void update() {

		// 增加
		UserName userName = new UserName();
		userName.initSingleTestData();
		Integer result = userNameDao.insertParmaryKey(userName);
		Assert.isTrue(result == 1, "插入结果:" + result);

		userName.setName("frankie1");

		result = userNameDao.update(userName);

		Assert.isTrue(result.intValue() == 1, "结果:" + (result.intValue() == 1));

		UserName userName2 = userNameDao.getUserNameById(userName.getId());

		Assert.isTrue(userName2.getName().equals("frankie1"), "结果:" + (userName2.getName().equals("frankie1")));

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 根据ID,name更新
	 */
	@Test
	public void updateParam() {

		// 增加
		UserName userName = new UserName();
		userName.initSingleTestData();
		Integer result = userNameDao.insertParmaryKey(userName);
		Assert.isTrue(result == 1, "插入结果:" + result);

		userName.setName("frankie1");
		result = userNameDao.insertParmaryKey(userName);

		userName.setAge(50);
		result = userNameDao.updateParam(userName);

		Assert.isTrue(result.intValue() == 1, "结果:" + (result.intValue() == 1));

		UserName userName2 = userNameDao.getUserNameById(userName.getId());

		Assert.isTrue(userName2.getAge().intValue() == 50, "结果:" + (userName2.getAge().intValue() == 50));

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 根据ID,name更新
	 */
	@Test
	public void updateById() {
		// 增加
		UserName userName = new UserName();
		userName.initSingleTestData();
		Integer result = userNameDao.insertParmaryKey(userName);
		Assert.isTrue(result == 1, "插入结果:" + result);

		userName.setAge(50);
		boolean updateResult = userNameDao.updateById(userName, userName.getId());
		Assert.isTrue(updateResult, "更新结果:" + updateResult);

		UserName userName2 = userNameDao.getUserNameById(userName.getId());

		Assert.isTrue(userName2.getAge().intValue() == 50, "验证更新数据:" + (userName2.getAge().intValue() == 50));

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

	/**
	 * 根据ID只更新NAME
	 */
	@Test
	public void updateSingleParam() {
		// 增加
		UserName userName = new UserName();
		userName.initSingleTestData();
		Integer result = userNameDao.insertParmaryKey(userName);
		Assert.isTrue(result == 1, "插入结果:" + (result == 1));

		result = userNameDao.updateSingleParam("frankietest", userName.getId());
		Assert.isTrue(result == 1, "更新结果:" + (result == 1));

		UserName userName2 = userNameDao.getUserNameById(userName.getId());

		Assert.isTrue(userName2.getName().equals("frankietest"), "验证更新数据:" + (userName2.getName().equals("frankietest")));

		// 删除
		Integer deleteResult = userNameDao.deleteByLike("frankie");
		Assert.isTrue(deleteResult > 0, "删除结果:" + deleteResult);
	}

}
