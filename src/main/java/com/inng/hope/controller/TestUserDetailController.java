package com.inng.hope.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inng.hope.result.ResultObject;
import com.inng.hope.services.TestUserDetailServices;
import com.inng.hope.services.TestUserServices;
import com.inng.hope.services.UserDaoServices;

@Controller
@RequestMapping("testUserDetail")
public class TestUserDetailController {

	
	
	@Resource(name="testUserDetailServicesImpl")
	private TestUserDetailServices testUserDetailServices;
	
	@Resource(name="userDaoServicesImpl")
	private UserDaoServices userDaoServices;
	
	
	

	
	@RequestMapping("getList.htm")
	public String getList(
			@RequestParam(value="pagination",defaultValue="1") Integer pagination,
			Map map){
		map.put("list", testUserDetailServices.getList());
	
		return "testUserDetail";
	}
	
	@RequestMapping("add.htm")
	public @ResponseBody int changePassword(@RequestParam(value="userName") String userName,
													 @RequestParam(value="age") String age,
													 @RequestParam(value="sex") String sex,
													 @RequestParam(value="phone") String phone,
													 @RequestParam(value="addDate") String addDate){
		
		return testUserDetailServices.add(userName, sex, age, phone, addDate);
	}
	
	@RequestMapping("toAdd.htm")
	public String toAdd(){
		return "addTestUserDetail";
	}
	
	
	
	
}
