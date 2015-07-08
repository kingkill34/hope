package com.inng.hope.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inng.hope.result.ResultObject;
import com.inng.hope.services.TestUserServices;
import com.inng.hope.services.UserDaoServices;

@Controller
@RequestMapping("testUser")
public class TestUserController {

	
	
	@Resource(name="testUserServicesImpl")
	private TestUserServices testUserServices;
	
	@Resource(name="userDaoServicesImpl")
	private UserDaoServices userDaoServices;
	
	
	

	
	@RequestMapping("getList.htm")
	public @ResponseBody ResultObject getList(
			@RequestParam(value="pagination",defaultValue="1") Integer pagination,
			HttpSession session){
		
		
		return userDaoServices.getList(pagination,session);
	}
	
	
	
	
	
	
	@RequestMapping("delUserById.htm")
	public @ResponseBody ResultObject delUserById(@RequestParam(value="userId") Integer userId){
		return userDaoServices.delUserById(userId);
	}
	
	@RequestMapping("addUser.htm")
	public @ResponseBody ResultObject addUser(@RequestParam(value="userName") String userName,
											  @RequestParam(value="password") String password){
		return userDaoServices.addUser(userName, password);
	}
	
	
	@RequestMapping("login.htm")
	public @ResponseBody int login(@RequestParam(value="userName") String userName,
								   @RequestParam(value="password") String password){
		return testUserServices.login(userName, password);
		//return "main";
	}
	
}
