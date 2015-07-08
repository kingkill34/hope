package com.inng.hope.services;


import javax.servlet.http.HttpSession;

import com.inng.hope.result.ResultObject;

public interface MemberDaoServices {

	
	ResultObject getList(Integer pagination,
			  			 Integer searchType,
			  			 String searchValue,
			  			 HttpSession session);
}
