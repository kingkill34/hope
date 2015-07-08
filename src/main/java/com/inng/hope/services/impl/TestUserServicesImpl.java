package com.inng.hope.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inng.hope.dao.TestUserDao;
import com.inng.hope.entity.TestUser;
import com.inng.hope.services.TestUserServices;


@Service(value="testUserServicesImpl")
public class TestUserServicesImpl implements TestUserServices {

	@Resource(name="testUserDao")
	private TestUserDao testUserDao;
	
	
	@Override
	public int login(String userName, String password) {
		
		
		TestUser testuser = testUserDao.login(userName, password);
		if(null!=testuser){
			testUserDao.updateNumber("0", userName);
			
			return 1;
		}
		
	
		
		try {
			int number = testUserDao.getNumber(userName);
			
			if(number==2){
				testUserDao.updateNumber("0", userName);
				testUserDao.updateFlag(userName,"1");
				return 3;
			}
			
			number++;
			testUserDao.updateNumber(String.valueOf(number), userName);
			
		} catch (Exception e) {
			return 2;
		}
		
		
		
		return 0;
	}

}
