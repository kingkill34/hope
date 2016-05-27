package com.duowan.hope.sql;

public class SqlOrContent {

	private String linkOperation;

	private String field;

	private String operation;

	private Object value;

	public SqlOrContent() {
	}

	public SqlOrContent(String linkOperation, String field, String operation, Object value) {
		this.linkOperation = linkOperation;
		this.field = field;
		this.operation = operation;
		this.value = value;
	}

	public String getLinkOperation() {
		return linkOperation;
	}

	public void setLinkOperation(String linkOperation) {
		this.linkOperation = linkOperation;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
