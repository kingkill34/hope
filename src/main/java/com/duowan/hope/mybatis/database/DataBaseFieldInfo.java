package com.duowan.hope.mybatis.database;

public class DataBaseFieldInfo {

	private String fieldName;
	private String fieldType;
	private String defaultValue;
	private boolean isPrimaryKey;
	private boolean isNullAble = false;
	private boolean isAutoincrement = false;

	public DataBaseFieldInfo(String fieldName, String fieldType, String nullAble, String defaultValue, String autoincrement, boolean isPrimaryKey) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.defaultValue = defaultValue;
		this.isPrimaryKey = isPrimaryKey;

		if (nullAble.equals("YES")) {
			isNullAble = true;
		}

		if (autoincrement.equals("YES")) {
			isAutoincrement = true;
		}
	}

	public boolean isInsert() {
		return !isPrimaryKey() && !isAutoincrement();
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public boolean isNullAble() {
		return isNullAble;
	}

	public void setNullAble(boolean isNullAble) {
		this.isNullAble = isNullAble;
	}

	public boolean isAutoincrement() {
		return isAutoincrement;
	}

	public void setAutoincrement(boolean isAutoincrement) {
		this.isAutoincrement = isAutoincrement;
	}

}
