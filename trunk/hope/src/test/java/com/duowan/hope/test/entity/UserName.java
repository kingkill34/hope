package com.duowan.hope.test.entity;

import java.util.Date;


public class UserName {

	private Integer id;

	private String name;

	private Integer age;

	private Date datetime;

	private Date time;

	private Double money;

	private Boolean isFirst;

	private Integer nums;

	private Date updateDate;

	public void initSingleTestData() {
		this.id = 1;
		this.name = "frankie";
		this.age = 30;
		this.datetime = new Date();
		this.time = new Date();
		this.money = 50.02;
		this.isFirst = true;
	}

	public void initListTestData(Integer i) {
		this.id = i;
		this.name = "frankie" + i;
		this.age = 30 * i;
		this.datetime = new Date();
		this.time = new Date();
		this.money = 50.02 * i;
		this.isFirst = true;
	}

	public void initListSameTestData(Integer i) {
		this.id = i;
		this.name = "frankie";
		this.age = 30 * i;
		this.datetime = new Date();
		this.time = new Date();
		this.money = 50.02 * i;
		this.isFirst = true;
	}

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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Boolean getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(Boolean isFirst) {
		this.isFirst = isFirst;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getNums() {
		return nums;
	}

	public void setNums(Integer nums) {
		this.nums = nums;
	}

}
