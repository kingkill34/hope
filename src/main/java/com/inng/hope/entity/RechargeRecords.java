package com.inng.hope.entity;

import java.util.Date;

public class RechargeRecords {

	/**
	 * 冲值编号
	 */
	private Integer id;
	
	/**
	 * 客户卡号
	 */
	private String card_id;
	
	/**
	 * 冲值日期
	 */
	private Date date;
	
	/**
	 * 冲值金额
	 */
	private double money;
	
	/**
	 * 操作员编号
	 */
	private Integer user_id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	
	
}
