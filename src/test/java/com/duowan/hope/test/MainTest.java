package com.duowan.hope.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.duowan.hope.mybatis.annotation.HopeCount;
import com.duowan.hope.mybatis.annotation.HopeInsert;
import com.duowan.hope.mybatis.annotation.HopeSelect;
import com.duowan.hope.mybatis.annotation.OP;
import com.duowan.hope.test.dao.UserDao;
import com.duowan.hope.test.entity.User;
import com.duowan.hope.test.entity.UserVo;

@ContextConfiguration(locations = "classpath*:/*context.xml")
public class MainTest extends AbstractJUnit4SpringContextTests {

	@Resource(name = "userDao")
	private UserDao userDao;

	
	
	
	@Test
	public void getUserTest() {
		User user = userDao.getUserTest("mouzemin1");
		System.out.println(user.getNickName());
	}
	
	
	@Test
	public void getUser() {
		User user = userDao.getUser("mouzemin1");
		System.out.println(user.getNickName());
	}

	@Test
	public void getUserList() {
		List<User> list = userDao.getUserList("frankie");
		for (User user : list) {
			System.out.println(user.getId());
		}
	}

	@Test
	public void getUserTwoParams() {
		User user = userDao.getUserTwoParams("frankie", "mouzemin2");
		System.out.println(user.getId());
	}

	@Test
	public void getUserTwoParamsList() {
		List<User> list = userDao.getUserTwoParamsList("frankie", 1);
		for (User user : list) {
			System.out.println(user.getId());
		}
	}

	@Test
	public void getUserMap() {
		Map<String, Object> map = userDao.getUserMap("mouzemin1");
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}

	@Test
	public void getUserListMap() {
		List<Map<String, Object>> list = userDao.getUserListMap("frankie");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				System.out.println(entry.getKey() + ":" + entry.getValue());
			}
		}

	}

	@Test
	public void getUserMapParam() {
		Map<String, Object> map = userDao.getUserMapParam("mouzemin1");
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}

	@Test
	public void getUserMapParams() {
		Map<String, Object> map = userDao.getUserMapParams("mouzemin1");
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}

	@Test
	public void getUserListMapParam() {
		List<Map<String, Object>> list = userDao.getUserListMapParam("mouzemin1");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				System.out.println(entry.getKey() + ":" + entry.getValue());
			}
		}
	}

	@Test
	public void getUserListMapParams() {
		List<Map<String, Object>> list = userDao.getUserListMapParams("mouzemin1");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				System.out.println(entry.getKey() + ":" + entry.getValue());
			}
		}
	}

	@Test
	public void getUserListGroupBy() {
		List<User> list = userDao.getUserListGroupBy("frankie");
		for (User user : list) {
			System.out.println(user.getId());
		}
	}

	@Test
	public void getUserCount() {
		Integer count = userDao.getUserCount("mouzemin1");
		System.out.println(count);
	}

	@Test
	public void getUserMapCount() {
		Map<String, Object> map = userDao.getUserMapCount("mouzemin1");
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}

	@Test
	public void testOP() {
		List<User> list = userDao.testOP(1);
		for (User user : list) {
			System.out.println(user.getId());
		}
	}

	@Test
	public void testTSN() {
		List<User> list = userDao.testTSN(1, 2022);
		for (User user : list) {
			System.out.println(user.getId());
		}
	}

	@Test
	public void insert() {
		User user = new User();
		user.setId(3);
		user.setLoginName("mouzemin3");
		user.setLoginPassword("dddddd");
		user.setNickName("frankie");
		user.setType(1);
		user.setGameId("2022");
		Integer result = userDao.insert(user);
		System.out.println(result);
	}

	@Test
	public void insertList() {
		List<User> list = new ArrayList<User>();
		for (int i = 0; i < 3; i++) {
			User user = new User();
			user.setLoginName("mouzemin" + i);
			user.setLoginPassword("dddddd");
			user.setNickName("frankie" + i);
			user.setType(1);
			user.setGameId("2022");
			list.add(user);
		}

		Integer result = userDao.insertList(list);
		System.out.println(result);
	}

	@Test
	public void insertByBoolean() {
		User user = new User();
		user.setLoginName("mouzemin3");
		user.setLoginPassword("dddddd");
		user.setNickName("frankie");
		user.setType(1);
		user.setGameId("2022");
		boolean result = userDao.insertByBoolean(user);
		System.out.println(result);
	}

	@Test
	public void insertParams() {
		Integer result = userDao.insertParams(1, "frankie", "mouzemin3", "dddddd");
		System.out.println(result);
	}

	@Test
	public void delete() {
		Integer result = userDao.delete("mouzemin3");
		System.out.println(result);
	}

	@Test
	public void deleteByBoolean() {
		boolean result = userDao.deleteByBoolean("mouzemin3");
		System.out.println(result);
	}

	@Test
	public void deleteGameId() {
		Integer result = userDao.deleteGameId(16, "2022");
		System.out.println(result);
	}

	@Test
	public void testDelete() {
		Integer result = userDao.testDelete(18, "2022");
		System.out.println(result);
	}

	@Test
	public void update() {
		User user = new User();
		user.setId(1);
		user.setLoginName("mouzemin333333");
		user.setNickName("frankie333");
		user.setType(1);
		user.setGameId("2022");
		Integer result = userDao.update(user);
		System.out.println(result);
	}

	@Test
	public void updateByParams() {
		User user = new User();
		user.setLoginName("mouzemin123");
		user.setNickName("frankie123");
		user.setType(1);
		user.setGameId("2022");
		Integer result = userDao.updateByParams(user, 4, "dddddd3333");
		System.out.println(result);
	}

	@Test
	public void updateFieldByParams() {
		Integer result = userDao.updateFieldByParams("kingkill33", 2, "dddddd");
		System.out.println(result);
	}

}
