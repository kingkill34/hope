package com.duowan.hope;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.util.StringUtils;

import com.duowan.hope.mybatis.util.FieldUtil;
import com.duowan.hope.sql.SqlOr;
import com.duowan.hope.sql.SqlOrContent;

public class Params extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 相等
	 */
	public static final String EQ_OPERATION = "=";

	/**
	 * 不相等
	 */
	public static final String NE_OPERATION = "!=";

	/**
	 * 小于
	 */
	public static final String LT_OPERATION = "<";

	/**
	 * 大于
	 */
	public static final String GT_OPERATION = ">";

	/**
	 * 小于等于
	 */
	public static final String LE_OPERATION = "<=";

	/**
	 * 大于等于
	 */
	public static final String GE_OPERATION = ">=";

	private static final String OR = "or";
	private static final String AND = "and";

	private static final String OPERATION = "operation";
	private static final String WHERE = "where";
	private static final String DISTINCT_FIELD = "distinctField";
	private static final String ORDER_BY = "order by";
	private static final String GROUP_BY = "group by";
	private static final String PAGE_START = "pageStart";
	private static final String PAGE_END = "pageEnd";

	// 数据库后缀，有时候我们需要分表 比如user表，user_2022,user_2020
	private static final String TABLE_NAME_SUFFIXES = "tableNameSuffixes";

	// 给默认值 "" 也算一个组
	private String orGroup = "";
	private List<SqlOr> orList = new ArrayList<SqlOr>();

	public Params() {
		setTableNameSuffixes(" ");
	}

	public Params(String tableNameSuffixes) {
		setTableNameSuffixes(tableNameSuffixes);
	}

	public Params(Object updateEntity) {
		setObjectValue(updateEntity);
		setTableNameSuffixes(" ");
	}

	public Params(List entityList) {
		Object obj = entityList.get(0);
		setObjectValue(obj);
		setTableNameSuffixes(" ");
		super.put("list", entityList);
	}

	public Params(String tableNameSuffixes, Object updateEntity) {
		setObjectValue(updateEntity);
		setTableNameSuffixes(tableNameSuffixes);
	}

	/**
	 * 相等
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Params eq(String key, Object value) {
		return put(key, EQ_OPERATION, value);
	}

	/**
	 * 不相等
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Params ne(String key, Object value) {
		return put(key, NE_OPERATION, value);
	}

	/**
	 * 小于
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Params lt(String key, Object value) {
		return put(key, LT_OPERATION, value);
	}

	/**
	 * 大于
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Params gt(String key, Object value) {
		return put(key, GT_OPERATION, value);
	}

	/**
	 * 小于等于
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Params le(String key, Object value) {
		return put(key, LE_OPERATION, value);
	}

	/**
	 * 大于等于
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Params ge(String key, Object value) {
		return put(key, GE_OPERATION, value);
	}

	/**
	 * or
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Params or(String groupId, String key, String operation, Object value) {
		if (StringUtils.isEmpty(groupId)) {
			this.orGroup = groupId;
		}
		SqlOrContent sqlOrContent = new SqlOrContent(OR, key, operation, value);
		return addOrContent(sqlOrContent);
	}

	/**
	 * or
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Params or(String key, String operation, Object value) {
		return or("", key, operation, value);
	}

	public Params setUpdateField(String fieldName, Object value) {
		super.put(fieldName, value);
		return this;
	}

	private Params put(String key, String operation, Object value) {
		super.put(OPERATION + key, operation);
		super.put(WHERE + key, value);
		return this;
	}

	public void setDistinctField(String field) {
		super.put(DISTINCT_FIELD, FieldUtil.toUnderlineName(field));
	}

	public void orderBy(String orderBy) {
		super.put(ORDER_BY, orderBy);
	}

	public void groupBy(String groupBy) {
		super.put(GROUP_BY, groupBy);
	}

	public void setPage(int currentPage, int size) {
		super.put(PAGE_START, currentPage - 1 * size);
		super.put(PAGE_END, currentPage * size);
	}

	public void setTableNameSuffixes(String tableNameSuffixes) {
		super.put(TABLE_NAME_SUFFIXES, tableNameSuffixes);
	}

	private void setObjectValue(Object updateEntity) {
		Field[] fields = updateEntity.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				super.put(field.getName(), field.get(updateEntity));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Params addOrContent(SqlOrContent sqlOrContent) {
		boolean isFind = false;
		for (SqlOr sqlOr : orList) {
			if (sqlOr.getGroup().equals(this.orGroup)) {
				sqlOr.add(sqlOrContent);
				isFind = true;
			}
		}

		if (!isFind) {
			SqlOr sqlOr = new SqlOr(orGroup, sqlOrContent);
			orList.add(sqlOr);
		}
		super.put("orList", orList);
		return this;
	}

	public List<SqlOr> getOrList() {
		return orList;
	}

}
