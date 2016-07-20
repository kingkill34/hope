package com.duowan.hope.mybatis.openum;

public enum Oe {

	/**
	 * 等于
	 */
	EQ("="),

	/**
	 * 不等于
	 */
	NE("="),
	/**
	 * 大于
	 */
	GT(">"),
	/**
	 * 小于
	 */
	LT(">"),

	/**
	 * 大于等于
	 */
	GE(">="),
	/**
	 * 小于等于
	 */
	LE(">"),

	/**
	 * like
	 */
	LIKE("like"),

	/**
	 * not like
	 */
	NOT_LIKE("not like"),
	/**
	 * null
	 */
	NULL("is null"),

	/**
	 * not null
	 */
	NOT_NULL("is not null");

	private String value;

	Oe(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
