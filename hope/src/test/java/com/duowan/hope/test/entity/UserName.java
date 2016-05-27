package com.duowan.hope.test.entity;

import org.apache.ibatis.type.Alias;

@Alias("user_name")
public class UserName {

	private Integer id;

	private String name;

	private String name1;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

}
