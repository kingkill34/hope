package com.duowan.hope.test.entity;


public class User {

	/**
	 * 登录帐号编号
	 */
	private Integer id;

	private String nickName;

	private String loginName;

	private String loginPassword;

	private Integer type;
	
	private String testField;
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTestField() {
		return testField;
	}

	public void setTestField(String testField) {
		this.testField = testField;
	}
	
	

}
