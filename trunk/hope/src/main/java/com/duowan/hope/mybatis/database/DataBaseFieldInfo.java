package com.duowan.hope.mybatis.database;

import com.duowan.hope.mybatis.annotation.OP;
import com.duowan.hope.mybatis.util.FieldUtil;

public class DataBaseFieldInfo {

	private static final String AS = " as ";
	private static final String DISTINCT = "distinct";

	private String fieldName;
	private String fieldType;
	private String operation;
	private String defaultValue;
	private boolean isPrimaryKey;
	private boolean isNullAble = false;
	private boolean isAutoincrement = false;
	private int fieldIndex;
	private boolean isDistinct = false;
	private OP op;
	private String specialField;

	public DataBaseFieldInfo(String fieldName, String fieldType, String nullAble, String defaultValue, String autoincrement, boolean isPrimaryKey, Integer index) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.defaultValue = defaultValue;
		this.isPrimaryKey = isPrimaryKey;
		this.fieldIndex = index;

		if (nullAble.equals("YES")) {
			isNullAble = true;
		}

		if (autoincrement.equals("YES")) {
			isAutoincrement = true;
		}
	}

	/**
	 * 获取查询字段
	 * 
	 * @return
	 */
	public String getSelectField(Integer fieldsLength, Integer i) {
		String fieldName = this.fieldName;
		String comma = getComma(fieldsLength, i);
		// field_name as fieldName,
		return fieldName + AS + FieldUtil.toCamelCase(fieldName) + comma;
	}

	public String getCountField() {
		String distinctStr = "";
		if (null != op && op.isDistinct()) {
			distinctStr = DISTINCT;
		}
		String fieldFormat = "count(" + distinctStr + " " + FieldUtil.toUnderlineName(value) + ")" + " as " + value;
		return fieldFormat;
	}

	public boolean isInsert() {
		return !isPrimaryKey() && !isAutoincrement();
	}

	/**
	 * 获取字段间隔,逗号
	 * 
	 * @param length
	 * @param i
	 * @return
	 */
	private String getComma(Integer length, Integer i) {
		String comma = "";
		if (null != length && i != null) {
			if (i != length - 1) {
				comma = ",";
			}
		}
		return comma;
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

	public int getFieldIndex() {
		return fieldIndex;
	}

	public void setFieldIndex(int fieldIndex) {
		this.fieldIndex = fieldIndex;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public OP getOp() {
		return op;
	}

	public void setOp(OP op) {
		this.op = op;
	}

	public boolean isDistinct() {
		return isDistinct;
	}

	public void setDistinct(boolean isDistinct) {
		this.isDistinct = isDistinct;
	}

	public String getSpecialField() {
		return specialField;
	}

	public void setSpecialField(String specialField) {
		this.specialField = specialField;
	}

}
