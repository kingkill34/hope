package com.duowan.hope.mybatis.initparams;

import java.util.List;

public class MapperParams {

	private String selectFields;

	private List<SqlField> sqlFidldList;

	public MapperParams(String selectFields, List<SqlField> sqlFidldList) {
		this.selectFields = selectFields;
		this.sqlFidldList = sqlFidldList;
	}

	public String getSelectFields() {
		return selectFields;
	}

	public void setSelectFields(String selectFields) {
		this.selectFields = selectFields;
	}

	public List<SqlField> getSqlFidldList() {
		return sqlFidldList;
	}

	public void setSqlFidldList(List<SqlField> sqlFidldList) {
		this.sqlFidldList = sqlFidldList;
	}

}
