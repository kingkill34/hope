package com.inng.hope.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class User {

	/**
	 * 登录帐号编号
	 */
	private Integer id;
	
	/**
	 * 登录名
	 */
	private String userName;
	
	/**
	 * 登录密码
	 */
	private String password;
	
	/**
	 * 注册时间
	 */
	private Date reg_time;
	
	/**
	 * 时间字符串类型
	 */
	private String reg_timeStr;
	
	
	public User(){}
	
	public User(String userName,String password){
		this.userName = userName;
		this.password = password;
		this.reg_time = new Date();
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getReg_time() {
		return reg_time;
	}

	public void setReg_time(Date reg_time) {
		this.reg_time = reg_time;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(null != reg_time){
			this.reg_timeStr = sdf.format(this.reg_time);
		}
		
	}

	public String getReg_timeStr() {
		return reg_timeStr;
	}

}
