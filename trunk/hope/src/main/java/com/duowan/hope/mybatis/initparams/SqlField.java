package com.duowan.hope.mybatis.initparams;

import com.duowan.hope.mybatis.database.MySqlDataType;
import com.duowan.hope.mybatis.util.FieldUtil;

public class SqlField {

	private String fieldName; // 字段名
	private String fieldNameSource; // 数据库原生字段
	private String fieldNameComma; // 带逗号的字段名
	private String whereFieldName; // WHERE条件中的字段名
	private String whereOperation; // WHERE条件中的操作符 = < >
	private String tableName; // 数据库名字 驼峰转下划线 userName 转换成 user_name
	private String dataType; // 数据库字段类型，如果为数字类型，判断为NULL即可。不需要=''

	private static final String PARAMS_OPERATION = "operation";
	private static final String PARAMS_WHERE = "where";

	public SqlField() {
	}

	public SqlField(String fieldName, String fieldNameComma, String tableName, String dataType) {
		this.fieldNameSource = fieldName;
		fieldName = FieldUtil.toCamelCase(fieldName);
		this.fieldName = fieldName;
		this.whereFieldName = PARAMS_WHERE + fieldName;
		this.whereOperation = PARAMS_OPERATION + fieldName;
		this.tableName = tableName;
		this.fieldNameComma = fieldName + fieldNameComma;
		this.dataType = dataType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getWhereFieldName() {
		return whereFieldName;
	}

	public void setWhereFieldName(String whereFieldName) {
		this.whereFieldName = whereFieldName;
	}

	public String getWhereOperation() {
		return whereOperation;
	}

	public void setWhereOperation(String whereOperation) {
		this.whereOperation = whereOperation;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldNameComma() {
		return fieldNameComma;
	}

	public void setFieldNameComma(String fieldNameComma) {
		this.fieldNameComma = fieldNameComma;
	}

	public String getFieldNameSource() {
		return fieldNameSource;
	}

	public void setFieldNameSource(String fieldNameSource) {
		this.fieldNameSource = fieldNameSource;
	}

	public boolean isNum() {
		return MySqlDataType.dataType.contains(dataType);
	}

}
