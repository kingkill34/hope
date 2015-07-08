package com.inng.hope.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inng.hope.dao.TestUserDetailDao;
import com.inng.hope.entity.TestUserDetail;
import com.inng.hope.services.TestUserDetailServices;

@Service(value="testUserDetailServicesImpl")
public class TestUserDetailServicesImpl implements TestUserDetailServices {

	
	@Resource(name="testUserDetailDao")
	private TestUserDetailDao testUserDetailDao;
	
	@Override
	public List<TestUserDetail> getList() {
		return testUserDetailDao.getList();
	}

	@Override
	public Integer add(String userName, String sex, String age, String phone,
			String addDate) {
		return testUserDetailDao.add(userName, sex, age, phone, addDate);
	}

}
