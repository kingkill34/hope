package com.inng.hope.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inng.hope.entity.Member;

public interface MemberDao {

	List<Member> getList(@Param("pagination")Integer pagination,
			   			 @Param("pageSize")Integer pageSize,
			   			 @Param("searchType") Integer searchType,
			   			 @Param("searchValue") String searchValue);
	
	Integer count(@Param("pagination")Integer pagination,
	  			  @Param("pageSize")Integer pageSize,
	  			  @Param("searchType") Integer searchType,
	  			  @Param("searchValue") String searchValue);
}
