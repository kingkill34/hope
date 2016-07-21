package com.duowan.hope.mybatis;

import com.duowan.hope.mybatis.util.FieldUtil;

public class TagReources {

	// mybatis tag
	public static final String ELEMENT_TYPE_SELECT = "select";

	public static final String ELEMENT_TYPE_INSERT = "insert";

	public static final String ELEMENT_TYPE_UPDATE = "update";

	public static final String ELEMENT_TYPE_DELETE = "delete";

	// mybatis tag

	// mybatis xml params
	public static final String MAPPER_ID = "id";

	public static final String MAPPER_PARAMETER_TYPE = "parameterType";

	public static final String MAPPER_RESULT_TYPE = "resultType";

	public static final String USE_GENERATED_KEYS = "useGeneratedKeys";

	public static final String KEY_PROPERTY = "keyProperty";

	// mybatis dynamics sql
	public static final String MAPPER_TABLE_SUFFIX = "<if test=\"%s != null \">%s</if>";
	public static final String IF_NOT_NULL = "<if test=\"#{%s} != null \">#{%s}%s</if>";
	public static final String COUNT = "count(%s) as %s";

	public static final String IS_NULL = " is null ";
	public static final String IS_NOT_NULL = " is not null ";
	public static final String LIKE = "like";
	public static final String AS = " as ";
	public static final String AND = " AND ";
	public static final String GROUP_BY = "group by ";
	public static final String ORDER_BY = "order by ";
	public static final String ASC = "asc";
	public static final String DESC = "desc";
	public static final String DISTINCT = "distinct ";

	// param
	public static final String PARAM = "param";
	public static final String ITEM_PARAM = "#{item.%s}";
	public static final String EXPRESSION = "#{%s}";

	// page
	public static final String PAGE_SIZE = "pageSize";
	public static final String PAGE_NO = "pageNo";

	// sql
	public static final String SQL_SELECT = "SELECT %s FROM <trim suffix='' suffixOverrides=' '>%s%s</trim> <where>%s</where> %s %s";
	public static final String SQL_INSERT = "INSERT INTO <trim suffix='' suffixOverrides=' '>%s%s</trim> (%s) VALUES %s";
	public static final String SQL_BATCH_INSERT_LIST = "INSERT INTO <trim suffix='' suffixOverrides=' '>%s%s</trim> (%s) VALUES <foreach collection='list' item='item' index='index' separator=','>%s</foreach>";
	public static final String SQL_BATCH_INSERT_ARRAY = "INSERT INTO <trim suffix='' suffixOverrides=' '>%s%s</trim> (%s) VALUES <foreach collection='array' item='item' index='index' separator=','>%s</foreach>";
	public static final String SQL_DELETE = "DELETE FROM <trim suffix='' suffixOverrides=' '>%s%s</trim> <where>%s</where>";
	public static final String SQL_UPDATE = "UPDATE <trim suffix='' suffixOverrides=' '>%s%s</trim> <set>%s</set> <where>%s</where>";

}
