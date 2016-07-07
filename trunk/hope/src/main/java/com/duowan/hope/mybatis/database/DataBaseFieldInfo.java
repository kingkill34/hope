package com.duowan.hope.mybatis.database;

import org.springframework.util.StringUtils;

import com.duowan.hope.mybatis.annotation.OP;
import com.duowan.hope.mybatis.util.FieldUtil;

public class DataBaseFieldInfo {

	private static final String AS = " as ";
	private static final String AND = " AND ";

	private String fieldName;
	private String fieldNameCamelCase;
	private String fieldType;
	private String operation;
	private String defaultValue;
	private boolean isPrimaryKey;
	private boolean isNullAble = false;
	private boolean isAutoincrement = false;
	private int fieldIndex;
	private boolean isDistinct = false;
	private boolean isEntityParam;
	private OP op;

	public DataBaseFieldInfo(String fieldName, String fieldType, String nullAble, String defaultValue, String autoincrement, boolean isPrimaryKey) {
		this.fieldName = fieldName;
		this.fieldNameCamelCase = FieldUtil.toCamelCase(fieldName);
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

	/**
	 * 获取查询字段
	 * 
	 * @return
	 */
	public String getSelectField(Integer fieldsLength, Integer i) {
		// field_name as fieldName,
		return fieldName + AS + this.fieldNameCamelCase + getComma(fieldsLength, i);
	}

	/**
	 * 获取插入字段,主键并且是自增长不插入
	 * 
	 * @param fieldsLength
	 * @param i
	 * @return
	 */
	public String getInsertField(Integer fieldsLength, Integer i) {
		String insertField = "";
		if (isInsert()) {
			insertField = this.fieldName + getComma(fieldsLength, i);
		}
		return insertField;
	}

	public String getInsertValue(Integer fieldsLength, Integer i, boolean isVoOrPo) {
		String ifNull = "<if test=\"#{%s} = null \"> %s</if>";
		String ifNotNull = "<if test=\"#{%s} != null \"> %s</if>";
		String insertValue = "";
		if (isInsert()) {
			String insertField = "";
			if (isVoOrPo) {
				insertField = "#{" + this.fieldNameCamelCase + "}" + getComma(fieldsLength, i);
			} else {
				insertField = "#{" + this.fieldIndex + "}" + getComma(fieldsLength, i);
			}

			if (!isNullAble && !StringUtils.isEmpty(defaultValue)) {// 不允许空，有默认值
				insertValue = String.format(ifNull, i, insertField);
				insertValue = String.format(ifNotNull, i, insertField);
			} else {// 插入字段值允许空 // 插入字段值不允许空，也没有默认值 //两种情况
				insertValue = insertField;
			}
		}
		return insertValue;
	}

	public String getBatchInsertValue(Integer fieldsLength, Integer i, boolean isVoOrPo) {
		String ifNull = "<if test=\"#{%s} = null \"> %s</if>";
		String ifNotNull = "<if test=\"#{%s} != null \"> %s</if>";
		String insertValue = "";
		if (isInsert()) {
			String insertField = "";
			if (isVoOrPo) {
				insertField = "#{item." + this.fieldNameCamelCase + "}" + getComma(fieldsLength, i);
			} else {
				insertField = "#{" + this.fieldIndex + "}" + getComma(fieldsLength, i);
			}

			if (!isNullAble && !StringUtils.isEmpty(defaultValue)) {// 不允许空，有默认值
				insertValue = String.format(ifNull, i, insertField);
				insertValue = String.format(ifNotNull, i, insertField);
			} else {// 插入字段值允许空 // 插入字段值不允许空，也没有默认值 //两种情况
				insertValue = insertField;
			}
		}
		return insertValue;
	}

	public String getUpdateValue(Integer fieldsLength, Integer i) {
		String updateValue = "";
		if (isEntityParam) {
			updateValue = this.fieldName + "=" + "#{" + this.fieldName + "}" + getComma(fieldsLength, i);
		} else {
			updateValue = "#{" + this.fieldIndex + "}" + getComma(fieldsLength, i);
		}
		return updateValue;
	}

	public String getWhereValue() {
		String value = "#{" + this.fieldIndex + "}";
		String whereValue = "=" + value;
		if (op != null) {
			if (!op.value().equals("")) {
				whereValue = op.value() + value;
			}

			if (op.isNull()) {
				whereValue = " is null";
			}

			if (op.isNotNull()) {
				whereValue = " is not null";
			}
		}
		return AND + fieldName + whereValue;
	}

	/**
	 * 判断是否为主键并且是自增长
	 * 
	 * @return
	 */
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

	public String getFieldNameCamelCase() {
		return fieldNameCamelCase;
	}

	public boolean isEntityParam() {
		return isEntityParam;
	}

	public void setEntityParam(boolean isEntityParam) {
		this.isEntityParam = isEntityParam;
	}

}
