package com.duowan.hope.sql;

import java.util.ArrayList;
import java.util.List;

public class SqlOr {

	private String group;

	private String linkOperation;

	private List<SqlOrContent> list;

	public SqlOr() {
		list = new ArrayList<SqlOrContent>();
	}

	public SqlOr(String group, SqlOrContent sqlOrContent) {
		list = new ArrayList<SqlOrContent>();
		this.group = group;
		// 默认第一个OR对象的操作符为括号外的操作符 and () or ()
		this.linkOperation = sqlOrContent.getLinkOperation();
		// 清空第一个OR对象的操作符,方便构建SQL
		sqlOrContent.setLinkOperation(" ");
		add(sqlOrContent);

	}

	public void add(SqlOrContent sqlOrContent) {
		list.add(sqlOrContent);
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<SqlOrContent> getList() {
		return list;
	}

	public void setList(List<SqlOrContent> list) {
		this.list = list;
	}

	public String getLinkOperation() {
		return linkOperation;
	}

	public void setLinkOperation(String linkOperation) {
		this.linkOperation = linkOperation;
	}

}
