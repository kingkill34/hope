package com.inng.hope.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inng.hope.entity.User;
import com.inng.hope.services.UserDaoServices;

@Controller
@RequestMapping("user")
public class UserController {

	@Resource(name = "userDaoServicesImpl")
	private UserDaoServices userDaoServices;

	// @RequestMapping("main")
	public String main() {
		return "main";
	}

	@RequestMapping
	public @ResponseBody String login(HttpSession session, String loginName, String loginPassword) {
		User user = userDaoServices.login(loginName, loginPassword);
		session.setAttribute(user.getId() + "", user);
		return main();
	}
	
	
	@RequestMapping
	public @ResponseBody String getById(HttpSession session,Integer id) {
		userDaoServices.getById(id);
		return main();
	}
	
	
	@RequestMapping
	public @ResponseBody String getList(HttpSession session,String loginName, String loginPassword) {
		userDaoServices.getList(loginName, loginPassword);
		return main();
	}
	
	@RequestMapping
	public @ResponseBody String insert(HttpSession session,User user) {
		userDaoServices.insert(user);
		return main();
	}
	
	
	@RequestMapping
	public @ResponseBody String delUserById(HttpSession session,User user) {
		userDaoServices.delById(user.getId());
		return main();
	}

}
