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
import com.inng.hope.services.MemberDaoServices;
import com.inng.hope.services.UserDaoServices;

@Controller
@RequestMapping("member")
public class MemberController {

	
	
	@Resource(name="memberDaoServicesImpl")
	private MemberDaoServices memberDaoServices;
	
	
	

	
	@RequestMapping("getList.htm")
	public @ResponseBody ResultObject getList(
			@RequestParam(value="pagination",defaultValue="1") Integer pagination,
			@RequestParam(value="searchType") Integer searchType,
			@RequestParam(value="searchValue") String searchValue,
			HttpSession session){
		return memberDaoServices.getList(pagination,searchType,searchValue,session);
	}
	
	
	
	
	
}
