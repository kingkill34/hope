package com.inng.hope.entity;

import java.util.Date;

public class ConsumingRecords {

	/**
	 * 消费记录编号
	 */
	private Integer id;
	
	/**
	 * 会员卡号
	 */
	private String card_id;
	
	/**
	 * 消费项目描述
	 */
	private String description;
	
	/**
	 * 消费金额
	 */
	private double money;
	
	/**
	 * 消费日期
	 */
	private Date date;
	
	/**
	 * 备注
	 */
	private String remark;
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
}
