package com.inng.hope.services.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.inng.hope.dao.MemberDao;
import com.inng.hope.entity.Member;
import com.inng.hope.result.ResultObject;
import com.inng.hope.result.code.MemberPrompt;
import com.inng.hope.services.MemberDaoServices;
import com.inng.hope.util.Page;

@Service(value = "memberDaoServicesImpl")
public class MemberDaoServicesImpl implements MemberDaoServices {

	@Resource(name = "memberDao")
	private MemberDao memberDao;

	@Override
	public ResultObject getList(Integer pagination, Integer searchType, String searchValue, HttpSession session) {
		// 分页对象
		Page page = null;
		ResultObject resultObject = null;
		try {
			if (pagination == 1) {
				page = new Page(pagination, 2, memberDao.count((pagination - 1) * 2, 2, searchType, searchValue), 3);
				session.setAttribute("page", page);
			} else {
				page = (Page) session.getAttribute("page");
				page.setChoosePage(pagination);
			}

			List<Member> memberList = memberDao.getList((pagination - 1) * 2, 2, searchType, searchValue);
			Assert.notNull(memberList);
			resultObject = new ResultObject(MemberPrompt.SUCCESS, memberList, page);
		} catch (Exception e) {
			e.printStackTrace();
			resultObject = new ResultObject(MemberPrompt.EXCEPTION, e);
		}
		return resultObject;
	}

}
