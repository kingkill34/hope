package com.duowan.hope.mybatis.initparams;

import com.duowan.hope.mybatis.annotation.Table;
import com.duowan.hope.mybatis.util.FieldUtil;

public class TableInfo {

	private String tableName;

	private String tableSuffix;

	private String tableSeparator;

	private String typeName;

	private Class<?> clz;

	private int index = -1;

	public TableInfo() {
	}

	public TableInfo(Table table) {
		this.clz = table.value();
		this.typeName = this.clz.getName();
		this.tableName =FieldUtil.toCamelCase(this.clz.getSimpleName());
		this.tableSuffix = table.tableSuffix();
		this.tableSeparator = table.tableSeparator();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableSuffix() {
		return tableSuffix;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public String getTableSeparator() {
		return tableSeparator;
	}

	public void setTableSeparator(String tableSeparator) {
		this.tableSeparator = tableSeparator;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}

}
