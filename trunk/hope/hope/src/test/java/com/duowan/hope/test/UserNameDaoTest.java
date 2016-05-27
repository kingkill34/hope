package com.duowan.hope.test;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.duowan.hope.Params;
import com.duowan.hope.test.dao.UserNameDao;
import com.duowan.hope.test.entity.UserName;

@ContextConfiguration(locations = "classpath*:/*context.xml")
public class UserNameDaoTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private UserNameDao userNameDao;

	

	@Test
	public void testtt() throws Exception {
		UserName userName = new UserName();
		userName.setName("testUserName");
		//userNameDao.testInsert(userName);
	}
	
	
	@Test
	public void test1() throws Exception {
		delAll(null);
		add(null);
		getUserById(null);
		update(null);
		del(null);

	}

	@Test
	public void test2() throws Exception {
		delAll("_2022");
		add("_2022");
		getUserById("_2022");
		update("_2022");
		del("_2022");
	}

	@Test
	public void test3() throws Exception {
		addList(null);
		getList(null);
		countDistinct(null);
		delAll(null);

	}

	private void addList(String TableNameSuffixes) {
		for (int i = 1; i < 10; i++) {
			UserName userName = new UserName();
			userName.setId(i);
			userName.setName("mouzemin");
			Integer result = 0;
			if (null == TableNameSuffixes) {
				// 添加用户
				result = userNameDao.insertH(userName);
			} else {
				Params Params = new Params(TableNameSuffixes, userName);
				result = userNameDao.insertByTnsH(Params);
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

		Params.eq("userName", "mouzemin");
		List<UserName> list = userNameDao.getListH(Params);

		Integer count = userNameDao.countH(Params);

		Assert.assertThat(count, Matchers.equalTo(list.size()));
	}

	private void countDistinct(String TableNameSuffixes) {
		Params Params = null;
		if (null == TableNameSuffixes) {
			Params = new Params();
		} else {
			Params = new Params(TableNameSuffixes);
		}

		Params.eq("userName", "mouzemin");
		Params.setDistinctField("userName");
		Integer count = userNameDao.countDistinctH(Params);

		Assert.assertThat(count, Matchers.equalTo(1));
	}

	private void delAll(String TableNameSuffixes) {
		if (null == TableNameSuffixes) {
			userNameDao.deleteH(new Params());
		} else {
			userNameDao.deleteH(new Params(TableNameSuffixes));
		}
	}

	private void add(String TableNameSuffixes) {
		UserName userName = new UserName();
		userName.setId(1);
		userName.setName("mouzemin");
		Integer result = 0;
		if (null == TableNameSuffixes) {
			// 添加用户
			result = userNameDao.insertH(userName);
		} else {
			Params Params = new Params(TableNameSuffixes, userName);
			result = userNameDao.insertByTnsH(Params);
		}
		Assert.assertThat(result, Matchers.equalTo(1));
	}

	// 根据ID获取用户
	private UserName getUserById(String TableNameSuffixes) {
		Params Params = null;
		if (null == TableNameSuffixes) {
			Params = new Params();
		} else {
			Params = new Params(TableNameSuffixes);
		}
		Params.eq("id", 1);
		UserName userName = userNameDao.getH(Params);
		Assert.assertNotNull(userName);
		return userName;
	}

	// 根据用户名密码获取用户
	private void update(String TableNameSuffixes) {
		UserName userName = new UserName();
		userName.setName("frankie1");
		Params Params = null;
		if (null == TableNameSuffixes) {
			Params = new Params(userName);
			Params.eq("id", 1);
			Integer result = userNameDao.updateH(Params);
			Assert.assertThat(result, Matchers.equalTo(1));
			userName = getUserById(null);
			Assert.assertNotNull(userName);
			Assert.assertThat(userName.getName(), Matchers.equalTo("frankie1"));
		} else {
			Params = new Params(TableNameSuffixes, userName);
			Params.eq("id", 1);
			Integer result = userNameDao.updateH(Params);
			Assert.assertThat(result, Matchers.equalTo(1));
			userName = getUserById(TableNameSuffixes);
			Assert.assertNotNull(userName);
			Assert.assertThat(userName.getName(), Matchers.equalTo("frankie1"));
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
		Integer result = userNameDao.deleteH(Params);
		Assert.assertThat(result, Matchers.equalTo(1));
	}
}
