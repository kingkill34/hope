package com.inng.hope.entity;

import java.util.Date;

public class Member {

	/**
	 * 会员编号
	 */
	private Integer id;
	
	/**
	 * 会员名称
	 */
	private String name;
	
	/**
	 * 会员卡号
	 */
	private String card_id;
	
	/**
	 * 会员电话
	 */
	private String mobile;
	
	/**
	 * 会员帐户余额
	 */
	private double balance;
	
	/**
	 * 注册会员时间
	 */
	private Date reg_time;
	
	/**
	 * 注册会员操作员编号
	 */
	private Integer user_id;

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

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Date getReg_time() {
		return reg_time;
	}

	public void setReg_time(Date reg_time) {
		this.reg_time = reg_time;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	
}
