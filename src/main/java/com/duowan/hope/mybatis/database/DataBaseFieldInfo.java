package com.duowan.hope.mybatis.database;

import org.springframework.util.StringUtils;

import com.duowan.hope.mybatis.TagReources;
import com.duowan.hope.mybatis.annotation.OP;
import com.duowan.hope.mybatis.util.FieldUtil;

public class DataBaseFieldInfo {

	private static final String YES = "YES";

	private String fieldName; // 字段名
	private String fieldNameCamelCase;
	private String fieldType;
	private String operation;
	private String defaultValue;
	private boolean isPrimaryKey;
	private boolean isNullAble = false;
	private boolean isAutoincrement = false;
	private int fieldIndex;
	private boolean isEntityParam;
	private boolean isSingleParam;
	private boolean isListOrArray;
	private OP op;

	public DataBaseFieldInfo(String fieldName, String fieldType, String nullAble, String defaultValue, String autoincrement, boolean isPrimaryKey) {
		this.fieldName = fieldName;
		this.fieldNameCamelCase = FieldUtil.toCamelCase(fieldName);
		this.fieldType = fieldType;
		this.defaultValue = defaultValue;
		this.isPrimaryKey = isPrimaryKey;
		this.isEntityParam = true;
		isNullAble = nullAble.equals(YES);
		isAutoincrement = autoincrement.equals(YES);
	}

	/**
	 * 获取查询字段
	 * 
	 * @return
	 */
	public String getSelectField(Integer fieldsLength, Integer i) {
		// field_name as fieldName,
		return fieldName + TagReources.AS + this.fieldNameCamelCase + getComma(fieldsLength, i);
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
		if (isModify()) {
			insertField = this.fieldName + getComma(fieldsLength, i);
		}
		return insertField;
	}

	public String getInsertValue(Integer fieldsLength, Integer i, boolean isVoOrPo) {
		String insertValue = "";
		if (isModify()) {
			String comma = getComma(fieldsLength, i);
			String insertField = "";
			if (isVoOrPo) {
				insertField = this.fieldNameCamelCase;
			} else {
				insertField = TagReources.PARAM + this.fieldIndex;
			}

			if (!isNullAble && !StringUtils.isEmpty(defaultValue)) {// 不允许空，有默认值
				insertValue = String.format(TagReources.IF_NOT_NULL, insertField, insertField, comma);
			} else {// 插入字段值允许空 // 插入字段值不允许空，也没有默认值 //两种情况
				insertValue = String.format(TagReources.EXPRESSION_HAS_KEY, insertField) + comma;
			}
		}
		return insertValue;
	}

	public String getBatchInsertValue(Integer fieldsLength, Integer i, boolean isVoOrPo) {
		String ifNotNull = "<if test=\"#{%s} != null \"> %s</if>";
		String insertValue = "";
		if (isModify()) {

			String insertField = "";
			if (isVoOrPo) {
				insertField = String.format(TagReources.ITEM_PARAM, this.fieldNameCamelCase) + getComma(fieldsLength, i);
			} else {
				insertField = String.format(TagReources.EXPRESSION_HAS_KEY, this.fieldIndex) + getComma(fieldsLength, i);
			}

			if (!isNullAble && !StringUtils.isEmpty(defaultValue)) {// 不允许空，有默认值
				insertValue = String.format(ifNotNull, i, insertField);
			} else {// 插入字段值允许空 // 插入字段值不允许空，也没有默认值 //两种情况
				insertValue = insertField;
			}
		}
		return insertValue;
	}

	public String getUpdateValue(Integer fieldsLength, Integer i) {
		String ifNotNull = "<if test=\"%s != null \"> %s</if>";
		String updateValue = "";
		String updateField = "";
		if (isModify()) {
			if (isEntityParam) {
				updateField = getMybatisParam() + this.fieldNameCamelCase;
			} else {
				updateField = TagReources.PARAM + this.fieldIndex;
			}
			updateValue = this.fieldName + TagReources.EQ + String.format(TagReources.EXPRESSION_HAS_KEY, updateField) + getComma(fieldsLength, i);
			updateValue = String.format(ifNotNull, updateField, updateValue);
		}
		return updateValue;
	}

	private String getMybatisParam() {
		String param = "";
		if (isSingleParam == false) {
			param = TagReources.PARAM + this.fieldIndex + ".";
		}
		return param;
	}

	public String getWhereValue() {
		String value = String.format(TagReources.EXPRESSION_HAS_KEY, TagReources.PARAM + this.fieldIndex);
		if (isEntityParam) {
			value = String.format(TagReources.EXPRESSION_HAS_KEY, getMybatisParam() + this.fieldNameCamelCase);
		}

		String whereValue = TagReources.EQ + value;
		if (op != null) {
			if (!op.value().equals("")) {
				if (op.value().equals(TagReources.LIKE)) {
					value = "\"%\"" + value + "\"%\"";
					whereValue = String.format(TagReources.CDATA, op.value()) + " " + value;
				}
			}

			if (op.isNull()) {
				whereValue = TagReources.IS_NULL;
			}

			if (op.isNotNull()) {
				whereValue = TagReources.IS_NOT_NULL;
			}
		}
		return TagReources.AND + fieldName + " " + whereValue;
	}

	/**
	 * 判断是否为主键并且是自增长
	 * 
	 * @return
	 */
	public boolean isModify() {
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

	public String getFieldNameCamelCase() {
		return fieldNameCamelCase;
	}

	public boolean isEntityParam() {
		return isEntityParam;
	}

	public void setEntityParam(boolean isEntityParam) {
		this.isEntityParam = isEntityParam;
	}

	public boolean isSingleParam() {
		return isSingleParam;
	}

	public void setSingleParam(boolean singleParam) {
		this.isSingleParam = singleParam;
	}

	public boolean isListOrArray() {
		return isListOrArray;
	}

	public void setListOrArray(boolean isListOrArray) {
		this.isListOrArray = isListOrArray;
	}

	public static void main(String[] args) {

	}

}
