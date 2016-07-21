package com.duowan.hope.test;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.duowan.hope.mybatis.page.HopePage;
import com.duowan.hope.test.dao.TestKeyIntDao;
import com.duowan.hope.test.entity.TestKeyInt;

@ContextConfiguration(locations = "classpath*:/*context.xml")
public class TestKeyIntTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private TestKeyIntDao testKeyIntDao;

	/**
	 * 测试增加数据返回Integer值
	 */
	@Test
	public void getById() {
		HopePage<TestKeyInt> page = testKeyIntDao.getListByName2("frankie", 20, 1);
		System.out.println(page);

		//
		// TestKeyInt tki = testKeyIntDao.getById(1);
		// System.out.println(page);
		//
		//
		// List<TestKeyInt> list = testKeyIntDao.getList("frankie");
		// System.out.println(list.size());

	}

}
