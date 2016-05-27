package com.duowan.hope.test;

import java.util.List;

import javax.annotation.Resource;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.duowan.hope.Params;
import com.duowan.hope.test.dao.UserDao;
import com.duowan.hope.test.entity.User;

@ContextConfiguration(locations = "classpath*:/*context.xml")
public class CRUDTest extends AbstractJUnit4SpringContextTests {

	@Resource(name = "userDao")
	private UserDao userDao;

	@Test
	public void tt() {
		User user = new User();
		user.setId(1);
		user.setLoginName("mouzemin");
		user.setLoginPassword("dddddd");
		user.setType(1);
		user.setNickName("frankie");
		// 添加用户
		userDao.insertH(user);
	}

	@Test
	public void test1() throws Exception {
		delAll(null);
		add(null);
		getUserById(null);
		login(null);
		update(null);
		del(null);

	}

	@Test
	public void test2() throws Exception {
		delAll("_2022");
		add("_2022");
		getList("_2022");
		getUserById("_2022");
		login("_2022");
		update("_2022");
		del("_2022");
	}

	@Test
	public void testOr() {

		Params params = new Params();
		params.or("group1", "loginName", Params.EQ_OPERATION, "mouzemin2");
		List<User> users = userDao.getList(params);
		System.out.println(users.size());
	}

	@Test
	public void test3() throws Exception {
		delAll(null);
		addList(null);
		getList(null);
		countDistinct(null);
		delAll(null);

	}

	@Test
	public void testUpdate() {
		delAll(null);
		add(null);

		Params params = new Params();
		params.setUpdateField("loginName", "mouzemin1");
		params.eq("loginName", "mouzemin");
		Integer result = userDao.updateH(params);
		Assert.assertThat(result, Matchers.equalTo(1));

		// delAll(null);
	}

	private void addList(String TableNameSuffixes) {
		for (int i = 1; i < 10; i++) {
			User user = new User();
			user.setId(i);
			user.setLoginName("mouzemin");
			user.setLoginPassword("dddddd");
			user.setType(1);
			user.setNickName("frankie");
			Integer result = 0;
			if (null == TableNameSuffixes) {
				// 添加用户
				result = userDao.insertH(user);
			} else {
				Params params = new Params(TableNameSuffixes, user);

				result = userDao.insertByTnsH(params);
			}
			Assert.assertThat(result, Matchers.equalTo(1));
		}
	}

	private void getList(String TableNameSuffixes) {
		Params Params = null;
		if (null == TableNameSuffixes) {
			Params = new Params();
		} else {
			Params = new Params(TableNameSuffixes);
		}

		Params.eq("loginName", "mouzemin");
		List<User> list = userDao.getListH(Params);

		Integer count = userDao.countH(Params);

		Assert.assertThat(count, Matchers.equalTo(list.size()));
	}

	private void countDistinct(String TableNameSuffixes) {
		Params Params = null;
		if (null == TableNameSuffixes) {
			Params = new Params();
		} else {
			Params = new Params(TableNameSuffixes);
		}

		Params.eq("loginName", "mouzemin");
		Params.setDistinctField("loginName");
		Integer count = userDao.countDistinctH(Params);

		Assert.assertThat(count, Matchers.equalTo(1));
	}

	private void delAll(String TableNameSuffixes) {
		if (null == TableNameSuffixes) {
			userDao.deleteH(new Params());
		} else {
			userDao.deleteH(new Params(TableNameSuffixes));
		}
	}

	private void add(String TableNameSuffixes) {
		User user = new User();
		user.setId(1);
		user.setLoginName("mouzemin");
		user.setLoginPassword("dddddd");
		user.setType(1);
		user.setNickName("frankie");
		Integer result = 0;
		if (null == TableNameSuffixes) {
			// 添加用户
			result = userDao.insertH(user);
		} else {
			Params params = new Params(TableNameSuffixes, user);
			result = userDao.insertByTnsH(params);
		}
		Assert.assertThat(result, Matchers.equalTo(1));
	}

	// 根据ID获取用户
	private User getUserById(String TableNameSuffixes) {
		Params Params = null;
		if (null == TableNameSuffixes) {
			Params = new Params();
		} else {
			Params = new Params(TableNameSuffixes);
		}
		Params.eq("id", 1);
		User user = userDao.getH(Params);
		Assert.assertNotNull(user);
		return user;
	}

	// 根据用户名密码获取用户
	private void login(String TableNameSuffixes) {
		Params Params = null;
		if (null == TableNameSuffixes) {
			Params = new Params();
		} else {
			Params = new Params(TableNameSuffixes);
		}
		Params.eq("loginName", "mouzemin").eq("loginPassword", "dddddd");
		User user = userDao.getH(Params);
		Assert.assertNotNull(user);
	}

	// 根据用户名密码获取用户
	private void update(String TableNameSuffixes) {
		User user = new User();
		user.setNickName("frankie1");
		Params Params = null;
		if (null == TableNameSuffixes) {
			Params = new Params(user);
			Params.eq("id", 1);
			Integer result = userDao.updateH(Params);
			Assert.assertThat(result, Matchers.equalTo(1));
			user = getUserById(null);
			Assert.assertNotNull(user);
			Assert.assertThat(user.getNickName(), Matchers.equalTo("frankie1"));
		} else {
			Params = new Params(TableNameSuffixes, user);
			Params.eq("id", 1);
			Integer result = userDao.updateH(Params);
			Assert.assertThat(result, Matchers.equalTo(1));
			user = getUserById(TableNameSuffixes);
			Assert.assertNotNull(user);
			Assert.assertThat(user.getNickName(), Matchers.equalTo("frankie1"));
		}

	}

	private void del(String TableNameSuffixes) {
		Params Params = null;
		if (null == TableNameSuffixes) {
			Params = new Params();

		} else {
			Params = new Params(TableNameSuffixes);
		}
		Params.eq("id", 1);
		Integer result = userDao.deleteH(Params);
		Assert.assertThat(result, Matchers.equalTo(1));
	}

}
